# Goals
* To translate a chat message to a basic JSON string making it easy to write commands for.


## Standard format
* The standard format is what all plugins recive, they should only ever respond with a string

#### Example: ```{"rank":1, "msg":"Hi!", "name", "Sir_Boops", "UUID","SoMe-RaNdOm-StRiNg"}```

### Ranks:
* Ranks are used to express what a users level is in the room
* 0: Guest: Somone who is not logged in.
* 1: The avarage user in the room
* 2: A channel subscriber
* 3: A mod in the channel
* 4: The channel owner