# concurrent-chat
An IRC-like chat. Use with [NetCat](https://nc110.sourceforge.io/) (nc) or [Nmap's implementation](https://nmap.org) (ncat).

## Usage

### Server

    
```console
java -jar concurrent-chat.jar
```
Console output:
> Starting Server on port 8080

The program will begin serving on port 8080 by default.

To choose a custom port use:

```console
java -jar concurrent-chat.jar [port]
```

Example: 

    java -jar concurrent-chat.jar 5678

Console output
> Starting Server on port 5678


### Client connections

```console
nc [host] [port]
```
Example: 
    
    nc 192.168.0.1 8080

>Note: **Ncat** (from Nmap) also works! Just replace nc with ncat. 

