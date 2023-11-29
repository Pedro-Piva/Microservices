import socket
import threading
from login import logar
from cadastro import cadastrar


def handle_client(client_socket, addr, logado):
    try:
        while True:
            request = client_socket.recv(1024).decode("utf-8")
            if request == "1":
                print(f"{addr[0]}:{addr[1]} Cadastro")
                client_socket.send("Cadastro\nInforme o Login: ".encode("utf-8"))
                login = client_socket.recv(1024).decode("utf-8")
                client_socket.send("Informe a Senha: ".encode("utf-8"))
                senha = client_socket.recv(1024).decode("utf-8")
                cadastro = cadastrar(login, senha)
                if cadastro != None:
                    print(f'Usuário {cadastro} cadastrado com Sucesso')
                    client_socket.send(f"Usuário {cadastro} cadastrado com Sucesso".encode("utf-8"))
                else:
                    client_socket.send("Login já existente!".encode("utf-8"))
            elif request == "2":
                print(f"{addr[0]}:{addr[1]} Login")
                client_socket.send("Login\nInforme o Login: ".encode("utf-8"))
                login = client_socket.recv(1024).decode("utf-8")
                client_socket.send("Informe a Senha: ".encode("utf-8"))
                senha = client_socket.recv(1024).decode("utf-8")
                if logar(login, senha):
                    logado = True
                    print("Logou")
                    client_socket.send("Logou!".encode("utf-8"))
                else:
                    client_socket.send("Login ou Senha Incorretos!".encode("utf-8"))
            elif request == "3":
                print("3")
            elif request == "4":
                print("4")
            elif request == "0":
                print("0")
            else:
                response = "novamente"
                client_socket.send(response.encode("utf-8"))
    except Exception as e:
        print(f"Error when hanlding client: {e}")
    finally:
        client_socket.close()
        print(f"Connection to client ({addr[0]}:{addr[1]}) closed")
        #raise KeyError


def run_server():
    server_ip = "localhost"  # server hostname or IP address
    port = 8000  # server port number
    # create a socket object
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
                target=handle_client,
                args=(
                    client_socket,
                    addr,
                    False,
                ),
            )
            thread.start()

    except Exception as e:
        print(f"Error: {e}")
    finally:
        server.close()


run_server()
