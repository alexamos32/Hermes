# Project Notes for Hermes
### Markdown
* Can use the commonmark java library
* Still need to figure out how to make use of it for live preview support.

### Navigation
* Still working on finding out if there are libraries to help this
* We have to options for doing this:
    * Folders and subfolders with notes inside
    * Or notes that can contain subnotes. So a note would act as both a folder and a note.
* Can probably use linked list in a tree structure to accomplish this.
* Need to consider how to re-order notes if we decide to reorder the list of notes. i.e. recently modified, alphabetical, etc. This can be considered more down the road.

### Voice Dictation
* Found a tutorial on text to speech. Fortunately Android has voice to text libraries.
* https://medium.com/voice-tech-podcast/android-speech-to-text-tutorial-8f6fa71606ac

### Addressing Proposal Concerns
* Will need to support using Markdown and basic text since not all users should be expected to know Markdown.
* Voice Dictation will only be used for text, not for writing Markdown. (Trying to voice Markdown would be a nightmare. You would constantly be saying Hashtag, Asterisk, etc.)
* Will most likely need a help page linking to some helpful Markdown tutorial resources. 

### Questions Encountered
* How to use commonmark to do live preview? Not just clicking a preview button after the page is complete.
* Would folder based hierarchy or linked notes hierarchy be more user friendly.

## TODO
* Spin up a basic page that writes text/markdown and converts it to HTML and displays it properly.
* Create a window for navigation that can be a sidebar, or brought up as a side bar from a button, etc.
* Once we have these two elements we can start working on page structure and how to structure the User Interface.
* Then start looking at implementing new features.