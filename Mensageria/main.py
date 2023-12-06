import socket
import threading
import time

global login_socket, cadastro_socket, pagamento_socket, produto_socket
login_socket = None
cadastro_socket = None
pagamento_socket = None
produto_socket = None

toCadastro = []
toLogin = []
toProduct = []
toPagamento = []


def cadastro(client_socket):
    while True:
        try:
            response = client_socket.recv(8192).decode("utf-8").strip("\x00")
            opcao = response.split("/")
            print(response)
            if opcao[0] == "LOGIN":
                toLogin.append(opcao)
            elif opcao[0] == "PAGAMENTO":
                toPagamento.append(opcao)
        except Exception as e:
            print("Erro cadastro_socket: " + e)
            global cadastro_socket
            cadastro_socket = None
        


def login(client_socket):
    while True:
        try:
            response = client_socket.recv(8192).decode("utf-8").strip("\x00")
            opcao = response.split("/")
            print(response)
            if opcao[0] == "PRODUTOS":
                toProduct.append(opcao)
        except Exception as e:
            print("Erro login_socket: " + e)
            global login_socket
            login_socket = None


def produto(client_socket):
    while True:
        try:
            response = client_socket.recv(8192).decode("utf-8").strip("\x00")
            opcao = response.split("/")
            print(response)
            if opcao[0] == "PAGAMENTO":
                toPagamento.append(opcao)
        except Exception as e:
            print("Erro login_socket: " + e)
            global produto_socket
            produto_socket = None


def pagamento(client_socket):
    while True:
        try:
            response = client_socket.recv(8192).decode("utf-8").strip("\x00")
            opcao = response.split("/")
            print(response)
            if opcao[0] == "PRODUTOS":
                toProduct.append(opcao)
        except Exception as e:
            print("Erro login_socket: " + e)
            global pagamento_socket
            pagamento_socket = None


def start_mensageria():
    server_ip = "localhost"
    port = 9999

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

    thread = threading.Thread(
        target=message_line_payment,
        args=(),
    )
    thread.start()

    try:
        server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server.bind((server_ip, port))
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
        print(f"Error MAIN: {e}")
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
            pagamento(client_socket)
    except Exception as e:
        print(f"Error: {e}")
    finally:
        client_socket.close()


def message_line_login():
    while True:
        global login_socket
        global toLogin
        if len(toLogin) != 0 and login_socket != None:
            print(toLogin)
            try:
                for item in toLogin:
                    enviar = item[1] + ":" + item[2]
                    login_socket.send(enviar.encode("utf-8"))
                    time.sleep(0.01)
                toLogin.clear()
            except Exception as e:
                login_socket = None
                print(f"Error message_login: {e}")
        time.sleep(0.01)

def message_line_product():
    while True:
        global produto_socket
        global toProduct
        if len(toProduct) != 0 and produto_socket != None:
            print(toProduct)
            try:
                for item in toProduct:
                    if item[1] == "LOGIN":
                        enviar = item[1] + ":" + item[2]
                        produto_socket.send(enviar.encode("utf-8"))
                    elif item[1] == "PAGAMENTO":
                        enviar = item[1] + ":" + item[2]
                        produto_socket.send(enviar.encode("utf-8"))
                    print("To_Product" ,end=" ")
                    print(item)
                    time.sleep(0.01)
                toProduct.clear()
            except Exception as e:
                produto_socket = None
                print(f"Error message_product: {e}")
                break
        time.sleep(0.01)


def message_line_payment():
    while True:
        global pagamento_socket
        global toPagamento
        if len(toPagamento) != 0 and toPagamento != None:
            print("To_PAGAMENTO: ", end="")
            print(toPagamento)
            try:
                for item in toPagamento:
                    if item[1] == "CADASTRO":
                        enviar = item[1] + ":" + item[2]
                        pagamento_socket.send(enviar.encode("utf-8"))
                    elif item[1] == "PRODUTO":
                        enviar = item[1] + ":" + item[2]
                        pagamento_socket.send(enviar.encode("utf-8"))
                    time.sleep(0.01)
                toPagamento.clear()
            except Exception as e:
                pagamento_socket = None
                print(f"Error message_payment: {e}")
                break
        time.sleep(0.01)

start_mensageria()
