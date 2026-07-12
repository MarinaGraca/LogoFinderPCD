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
* bottom buttons
  * button to select directory to search (*Pasta*)
  * button to select image/logo to find in the selected folder (*Imagem*)
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

Start the Worker: `java Worker localhost 8080 "Procura Simples"`\
Output:
```
Worker thread
Connected to server socket: Socket[addr=localhost/127.0.0.1,port=8080,localport=54654]
Address: localhost/127.0.0.1
```

```
Server running
Server Socket: ServerSocket[addr=0.0.0.0/0.0.0.0,localport=8080]
Conexao aceite na socket: Socket[addr=/127.0.0.1,port=54641,localport=8080]
Conexao aceite na socket: Socket[addr=/127.0.0.1,port=54654,localport=8080]
```

1. On the left pannel, choose the search orientation you want - *Procura Simples*, *Procura 90º*, or *Procura 180º* - to search for logos rotated at 0º, 90º and 180º angles.
2. Click on the *Pasta* button to choose the image folder where you want to search.
3. Click on the *Imagem* button to choose the logo.
4. Finally, click on the *Procura* button to start the search.
When the search is over, you can select the image you want to see on the list on the right and it will appear on the center.

```
Client thread:
Connected to server socket: Socket[addr=localhost/127.0.0.1,port=8080,localport=54641]
Address: localhost/127.0.0.1
Procura Simples
Nova Procura...
Imagem: image1_1.png
```

```
Worker thread
Connected to server socket: Socket[addr=localhost/127.0.0.1,port=8080,localport=54654]
Address: localhost/127.0.0.1
...\LogoFinderPCD\img\out\image1_1.png
(63, 177)
(334, 289)
(649, 88)
(870, 291)
...\LogoFinderPCD\img\out\image1_2.png
...
```
