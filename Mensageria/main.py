import socket
import threading

global login_socket, cadastro_socket, pagamento_socket, produto_socket
login_socket = None
cadastro_socket = None
pagamento_socket = None
produto_socket = None

toLogin = []
toProduct = []
toPagamento = []

def cadastro(client_socket):
    while True:
        response = client_socket.recv(8192).decode("utf-8")
        print(response)
        toLogin.append(response.strip("\x00"))

def login(client_socket):
    while True:
        response = client_socket.recv(1024).decode("utf-8")
        print(response)
        toProduct.append(response.strip("\x00"))
        print(toProduct)

def produto(client_socket):
    while True:
        response = client_socket.recv(1024).decode("utf-8")
        print(response)
        toPagamento.append(response.strip("\x00"))
        print(toPagamento)

def start_mensageria():
    server_ip = "localhost"  # server hostname or IP address
    port = 9999  # server port number

    thread = threading.Thread(
        target=message_line_login,
        args=(),
    )
    thread.start()
    
    thread = threading.Thread(
        target=message_line_product,
        args=(),
    )
    thread.start()

    #thread = threading.Thread(
    #    target=message_line_payment,
    #    args=(),
    #)
    #thread.start()
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
        tipo = client_socket.recv(2048).decode("utf-8")
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
            global pagamento_socket
            pagamento_socket = client_socket
            thread = threading.Thread(
                target=cadastro,
                args=(client_socket,),
            )
            thread.start()
    except Exception as e:
        print(f"Error: {e}")
    finally:
        client_socket.close()


def message_line_login():
    while True:
        global login_socket
        if len(toLogin) != 0:
            try:
                for item in toLogin:
                    login_socket.send(item.encode("utf-8"))
                toLogin.clear()
            except Exception as e:
                login_socket = None
                print(f"Error: {e}")
                break

def message_line_product():
    while True:
        global produto_socket
        if len(toProduct) != 0:
            try:
                for item in toProduct:
                    produto_socket.send(item.encode("utf-8"))
                toProduct.clear()
            except Exception as e:
                produto_socket = None
                print(f"Error: {e}")
                break

def message_line_payment():
     while True:
        global pagamento_socket
        if len(toPagamento) != 0:
            try:
                for item in toPagamento:
                    pagamento_socket.send(item.encode("utf-8"))
                toPagamento.clear()
            except Exception as e:
                pagamento_socket = None
                print(f"Error: {e}")
                break
start_mensageria()
