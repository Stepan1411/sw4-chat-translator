# This mod translates Minecraft chat into multiple languages!

### Supported languages:
- English (english/en)
- French (french/fr)
- German (german/de)
- Russian (russian/ru)
- Spanish (spanish/es)

### Example
![Replace this with a description](https://cdn.modrinth.com/data/cached_images/9c3f71dbcaaf54c0c0b354e4d56f1e8c8f30e81d_0.webp)

### How select language?

```
/sw4-translator mylanguage spanish/russian/german/french/english
```

### How does the mod translate messages?

Message Interception - The ChatHud.addMessage() mixin intercepts all incoming chat messages.

Text Extraction - Parses the message, separating the player name from the text:

<Player> hello → Player: "Player", text: "hello"

HTTP Request to Google Translate - Sends an asynchronous GET request:


```
https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=ru&dt=t&q=hello
```

sl=auto - autodetect source language

tl=ru - disable language (your selected one)

q=hello - text to translate

JSON Response Parsing - Extracts the translated text from Google's JSON response
Add to Chat - Adds a new message with the translation:

Original: <Player>, hello!

Translation: <Player> привет
