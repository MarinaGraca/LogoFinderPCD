# LogoFinderPCD
### PCD Project - Concurrent and Distributed Programming
### 3º Year, 1º Semester | 2019-2020

We want to search for a sub-image (*logo*) in a set of bigger images.\
The Client will connect to the Server and ask for it to find the *logo*.\
The Server will ask various Workers to find this *logo* in a concurrent way.\
(...)\
At the end of the search, when the user selects one of the images where the *logo* was found, the image will appear at the center of the frame with the *logo* marked in red.

GUI specs:
* left pannel to choose picture search orientation
* right pannel to show the logo search results
* central pannel where the selected image will appear
* directory to search (*Pasta*)
* image/logo to find in the selected folder (*Imagem*)
* search button (*Procura*)

Start the Server: `java Server 8080`\
Output:
```
Server running
Server Socket: ServerSocket[addr=0.0.0.0/0.0.0.0,localport=8080]
```

Start the Client: `java Client localhost 8080`\
Output:
```
Client thread
Connected to server socket: Socket[addr=localhost/127.0.0.1,port=8080,localport=54641]
Address: localhost/127.0.0.1
```

Start the Worker: `java Worker localhost 8080 "Procura Simples"`
Output:
```
Server running
Server Socket: ServerSocket[addr=0.0.0.0/0.0.0.0,localport=8080]
Conexao aceite na socket: Socket[addr=/127.0.0.1,port=54641,localport=8080]
Conexao aceite na socket: Socket[addr=/127.0.0.1,port=54654,localport=8080]
