# LogoFinderPCD
### PCD Project - Concurrent and Distributed Programming
### 3º Year, 1º Semester | 2019-2020

We want to search for a sub-image (*logo*) in a set of bigger images.\
The Client will connect to the Server and ask for it to find the *logo*.\
The Server will ask various Workers to find this *logo* in a concurrent way.\
(...)\
At the end of the search, when the user selects one of the images where the *logo* was found, the image will appear at the center of the frame with the *logo* marked in red.\

Start the Server: `java Server 8080`\
Start the Client: `java Client localhost 8080`\
Start the Worker: `java Worker localhost 8080` (...)
