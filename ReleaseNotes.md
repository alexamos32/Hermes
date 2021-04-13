# Release 1.0
**What’s new.**
Version 1.0 brings forth a fully fledged note taking application with a plethora of useful productivity features. Take notes in both text format or display notes with beautifully formatted HTML using the built in Markdown interpreter. Simply, type away and click preview in any note editing window to see the final output.

We are constantly working to improve user experience and committed to building the ultimate productivity tool. Moving forward we will continue fixing known bugs and adding useful new features to Hermes.

**Note:** Currently due to the issue of loading multiple images into the layout, this feature as been left separately on the image_capture branch. Once it has been fixed it will be merged with the rest of the application

**Features.**
* Markdown Support
* Load images into notes
* Set reminders
* Email text and HTML notes seamlessly
* Capture notes verbally with voice-to-text dictation
* Navigate with nested folders

**Known issues.**
Issue loading more than one image into the layout. Plan in place to fix it with a recycler view in a gridlayout format.
Images captured from the camera are displayed in an improper rotation.
Folders cannot be deleted or modified after created.
Navigating through folders and cancelling an action (such as create a folder/note) returns the user to the root folder.

**Supported API.**
The current minimum supported API is API 23 (Marshmallow). Hermes supports up to API 30 (Android Q). 
