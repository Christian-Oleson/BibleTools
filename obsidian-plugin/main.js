'use strict';

const obsidian = require('obsidian');

const DEFAULT_SETTINGS = {
    apiKey: '',
    defaultFolder: ''
};

class PassageModal extends obsidian.Modal {
    constructor(app, onSubmit) {
        super(app);
        this.onSubmit = onSubmit;
        this.passage = '';
    }

    onOpen() {
        const { contentEl } = this;
        contentEl.createEl('h2', { text: 'Enter Bible Passage' });

        const inputEl = contentEl.createEl('input', {
            type: 'text',
            placeholder: 'e.g., John 3:16, Romans 8:28-39'
        });
        inputEl.style.width = '100%';
        inputEl.style.marginBottom = '1em';
        inputEl.addEventListener('input', (e) => {
            this.passage = e.target.value;
        });
        inputEl.addEventListener('keydown', (e) => {
            if (e.key === 'Enter') {
                e.preventDefault();
                this.submit();
            }
        });

        const buttonContainer = contentEl.createDiv();
        buttonContainer.style.display = 'flex';
        buttonContainer.style.justifyContent = 'flex-end';
        buttonContainer.style.gap = '8px';

        const cancelBtn = buttonContainer.createEl('button', { text: 'Cancel' });
        cancelBtn.addEventListener('click', () => this.close());

        const submitBtn = buttonContainer.createEl('button', { text: 'Fetch', cls: 'mod-cta' });
        submitBtn.addEventListener('click', () => this.submit());

        inputEl.focus();
    }

    submit() {
        if (this.passage.trim()) {
            this.close();
            this.onSubmit(this.passage.trim());
        }
    }

    onClose() {
        const { contentEl } = this;
        contentEl.empty();
    }
}

class ESVSettingTab extends obsidian.PluginSettingTab {
    constructor(app, plugin) {
        super(app, plugin);
        this.plugin = plugin;
    }

    display() {
        const { containerEl } = this;
        containerEl.empty();

        containerEl.createEl('h2', { text: 'ESV Bible Settings' });

        new obsidian.Setting(containerEl)
            .setName('ESV API Key')
            .setDesc('Get your API key from https://api.esv.org/')
            .addText(text => text
                .setPlaceholder('Enter your API key')
                .setValue(this.plugin.settings.apiKey)
                .onChange(async (value) => {
                    this.plugin.settings.apiKey = value;
                    await this.plugin.saveSettings();
                }));

        new obsidian.Setting(containerEl)
            .setName('Default folder for new notes')
            .setDesc('Leave empty to create in vault root')
            .addText(text => text
                .setPlaceholder('e.g., Bible Notes')
                .setValue(this.plugin.settings.defaultFolder)
                .onChange(async (value) => {
                    this.plugin.settings.defaultFolder = value;
                    await this.plugin.saveSettings();
                }));
    }
}

class ESVBiblePlugin extends obsidian.Plugin {
    async onload() {
        await this.loadSettings();

        this.addCommand({
            id: 'insert-passage',
            name: 'Insert passage at cursor',
            editorCallback: (editor, view) => {
                this.promptAndFetch((markdown) => {
                    editor.replaceSelection(markdown);
                });
            }
        });

        this.addCommand({
            id: 'create-passage-note',
            name: 'Create new note with passage',
            callback: () => {
                this.promptAndFetch(async (markdown, passage) => {
                    const fileName = this.sanitizeFileName(passage);
                    let folderPath = this.settings.defaultFolder;

                    if (folderPath && !this.app.vault.getAbstractFileByPath(folderPath)) {
                        await this.app.vault.createFolder(folderPath);
                    }

                    const filePath = folderPath
                        ? `${folderPath}/${fileName}.md`
                        : `${fileName}.md`;

                    const file = await this.app.vault.create(filePath, markdown);
                    await this.app.workspace.getLeaf().openFile(file);
                });
            }
        });

        this.addSettingTab(new ESVSettingTab(this.app, this));
    }

    promptAndFetch(callback) {
        if (!this.settings.apiKey) {
            new obsidian.Notice('Please set your ESV API key in plugin settings');
            return;
        }

        new PassageModal(this.app, async (passage) => {
            try {
                new obsidian.Notice(`Fetching ${passage}...`);
                const markdown = await this.fetchPassage(passage);
                callback(markdown, passage);
                new obsidian.Notice('Passage inserted!');
            } catch (error) {
                console.error('ESV Bible error:', error);
                new obsidian.Notice(`Error: ${error.message}`);
            }
        }).open();
    }

    async fetchPassage(passage) {
        const url = new URL('https://api.esv.org/v3/passage/html/');
        url.searchParams.set('q', passage);
        url.searchParams.set('include-footnotes', 'true');
        url.searchParams.set('include-audio-link', 'true');
        url.searchParams.set('include-short-copyright', 'true');

        const response = await fetch(url.toString(), {
            headers: {
                'Authorization': `Token ${this.settings.apiKey}`
            }
        });

        if (!response.ok) {
            throw new Error(`API error: ${response.status}`);
        }

        const data = await response.json();

        if (!data.passages || data.passages.length === 0) {
            throw new Error('No passage found');
        }

        return this.convertToMarkdown(data);
    }

    convertToMarkdown(data) {
        let markdown = '';

        // Title
        markdown += `# ${data.canonical}\n\n`;

        // Process each passage
        for (const html of data.passages) {
            // Convert HTML to Markdown (basic conversion)
            let text = html;

            // Remove verse numbers in brackets and convert to headers
            text = text.replace(/\[(\d+)\]/g, '\n\n**$1** ');

            // Convert common HTML elements
            text = text.replace(/<h\d[^>]*>(.*?)<\/h\d>/gi, '### $1\n\n');
            text = text.replace(/<p[^>]*>/gi, '\n');
            text = text.replace(/<\/p>/gi, '\n');
            text = text.replace(/<br\s*\/?>/gi, '\n');
            text = text.replace(/<b>(.*?)<\/b>/gi, '**$1**');
            text = text.replace(/<i>(.*?)<\/i>/gi, '*$1*');
            text = text.replace(/<em>(.*?)<\/em>/gi, '*$1*');
            text = text.replace(/<strong>(.*?)<\/strong>/gi, '**$1**');
            text = text.replace(/<span[^>]*>(.*?)<\/span>/gi, '$1');
            text = text.replace(/<a[^>]*href="([^"]*)"[^>]*>(.*?)<\/a>/gi, '[$2]($1)');
            text = text.replace(/<sup>(.*?)<\/sup>/gi, '^$1^');

            // Remove remaining HTML tags
            text = text.replace(/<[^>]+>/g, '');

            // Clean up whitespace
            text = text.replace(/\n{3,}/g, '\n\n');
            text = text.trim();

            // Decode HTML entities
            text = text.replace(/&quot;/g, '"');
            text = text.replace(/&amp;/g, '&');
            text = text.replace(/&lt;/g, '<');
            text = text.replace(/&gt;/g, '>');
            text = text.replace(/&nbsp;/g, ' ');
            text = text.replace(/&#39;/g, "'");

            markdown += text + '\n\n';
        }

        markdown += '---\n*ESV*\n';

        return markdown;
    }

    sanitizeFileName(name) {
        return name.replace(/[\\/:*?"<>|]/g, '-');
    }

    async loadSettings() {
        this.settings = Object.assign({}, DEFAULT_SETTINGS, await this.loadData());
    }

    async saveSettings() {
        await this.saveData(this.settings);
    }
}

module.exports = ESVBiblePlugin;
