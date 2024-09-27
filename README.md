
1- Download Go Project 
[go.zip](https://github.com/user-attachments/files/17097881/go.zip)

2- Download Docker Desktop App https://www.docker.com/products/docker-desktop/
 - u may need to download and setup wsl.2.1.5.0.x64 to works docker well

3- open terminal inside go folder

4- run   docker build -t go-upload-app .

5- run  docker run -p 8080:8080 go-upload-app


- Successful attempt
![image](https://github.com/user-attachments/assets/f2f1b5fd-e92d-47cc-869b-7f144d2ab2a8)

- Interruption occurred, started resumption flow
![image](https://github.com/user-attachments/assets/c055b8b3-de5b-4962-8b6d-2a54b2c8abb9)


Challenge 1 : Accept 2 responses from the same request:
the first challenge is that android clients don’t accept 2 responses from the same request .
in android , except HttpUrlConnection , all above clients are higher-level abstraction for HTTP communication
we tried OkHttp and retrofit to add Both of Application Interceptor and Network Interceptor
we tried every client of above in a single structure but there a client has received only 104 response code but closed the connection
directly , and another received only 201 Created as the final result and also closed the connection directly , SO ? no client could to
receive 2 responses at the same request since android already as a low level don’t support 2 responses from a single request.
None of above libraries natively supports receiving multiple distinct responses from a single HTTP request because that goes against the
design of the HTTP protocol itself.

Challenge 2 : Receive 104 Response Code:
the second challenge in android to receive response code under standard response codes 200
104 is informational response code that is already under 200 response codes
we tried every client of above in a single structure , all clients have received 201 Created as a final response and closed the connection
directly, except HttpUrlConnection, it has received 104 as a final response but the same problem of closing connection directly , since
we already proved above that android clients have been designed to receive single response from a single request.
SO ? HttpUrlConnection is the only client that accept and read the first response 104 as final result but the root problem here that it
also close the connection directly! but HttpUrlConnection is low-level layer that needs more boilerplate code to be customized!


Brainstorming:
to solve this challenge may need to implement SocketIO (WebSockets) (with OkHttp) or other non-HTTP protocols might be necessary, as
neither HttpURLConnection , Volley , OkHttp , nor Retrofit natively support multiple responses for a single HTTP request,
- WebSockets solution will be overhead on the backend team as well it’s not the best practice for resuming file upload generally.
- New API backend can design a new API for retrieving the location URL then use it in post upload API ?
i’s the same solution in legacy publish report API that is invoked only to create a new report remotely and return server id to allow clients
uploading report on specific ID , but it’s may is overhead on backend to make a new API and also not mirror structure to IOS team , which
may in the future causes more boilerplate codes on the same modification.
Solution: Webhook approach?
WebHook allows one system to notify another automatically. This is done by making an HTTP request (usually POST) to a pre-configured
URL whenever an event occurs.


Successful scenario:
1. use HttpUrlConnection to invoke POST Upload API , it will respond with location URL.
2. invoke Head API with saved location URL to get offset that is by default 0 since it is the first upload , but this step is for a robust structure
.
3. invoke Patch Upload API with saved location URL and offset 0.


Interruption scenario:
1. invoke Head API with saved location URL to get offset , since we already saved location URL before in our local DB.
2. invoke Patch Upload API with saved location URL with returned Uploaded-Offset.


Full Challenge here :)
[Android Resumable Upload.pdf](https://github.com/user-attachments/files/17170718/Android.Resumable.Upload.pdf)


