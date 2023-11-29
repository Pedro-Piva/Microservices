import socket
import threading

login_socket = None
toLogin = []

def cadastro(client_socket):
    while True:
        response = client_socket.recv(8192).decode("utf-8")
        print(response)
        toLogin.append(response.strip("\x00"))

def login(client_socket):
    while True:
        response = client_socket.recv(1024).decode("utf-8")
        print(response)
        toLogin.append(response.strip("\x00"))
        print("Login")

def produto(client_socket):
    while True:
        response = client_socket.recv(1024).decode("utf-8")
        print(response)
        toLogin.append(response.strip("\x00"))

def start_mensageria():
    server_ip = "localhost"  # server hostname or IP address
    port = 9999  # server port number

    thread = threading.Thread(
        target=message_line,
        args=(),
    )
    thread.start()
    try:
        server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        # bind the socket to the host and port
        server.bind((server_ip, port))
        # listen for incoming connections
        server.listen()
        print(f"Listening on {server_ip}:{port}")

        while True:
            client_socket, addr = server.accept()
            print(f"Accepted connection from {addr[0]}:{addr[1]}")
            thread = threading.Thread(
                target=server_accept,
                args=(client_socket,),
            )
            thread.start()
    except Exception as e:
        print(f"Error: {e}")
    finally:
        server.close()



def server_accept(client_socket):
    try:
        tipo = client_socket.recv(8192).decode("utf-8")
        print(f"Type: {tipo}")
        if tipo == "Cadastro":
            global cadastro_socket
            cadastro_socket = client_socket
            cadastro(client_socket)
        elif tipo == "Login":
            global login_socket
            login_socket = client_socket
            login(client_socket)
        elif tipo == "Produto":
            global produto_socket
            produto_socket = client_socket
            produto(client_socket)
        elif tipo == "Pagamento":
            thread = threading.Thread(
                target=cadastro,
                args=(client_socket,),
            )
            thread.start()
    except Exception as e:
        print(f"Error: {e}")
    finally:
        client_socket.close()


def message_line():
    while True:
        if len(toLogin) != 0:
            try:
                for item in toLogin:
                    login_socket.send(item.encode("utf-8"))
                toLogin.clear()
            except Exception as e:
                print(f"Error: {e}")

start_mensageria()
