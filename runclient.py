import socket
import time

def cadastro(nome, login, senha, idade, saldo):
    try:
        client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server_ip = "localhost"
        server_port = 8081
        client.connect((server_ip, server_port))
        client.send(nome.encode("utf-8")[:4096])
        time.sleep(0.01)
        client.send(login.encode("utf-8")[:4096])
        time.sleep(0.01)
        client.send(senha.encode("utf-8")[:4096])
        time.sleep(0.01)
        client.send(idade.encode("utf-8")[:4096])
        time.sleep(0.01)
        client.send(saldo.encode("utf-8")[:4096])
        response = client.recv(4096).decode("utf-8")
        if response == "FIM":
            return True
        else:
            return False
    except Exception as e:
        print(f"Erro no cadastro: {e}")
        return False
    finally:
        client.close()

def login(login, senha):
    try:
        client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server_ip = "localhost"
        server_port = 8082
        client.connect((server_ip, server_port))
        client.send(login.encode("utf-8")[:4096])
        time.sleep(0.01)
        client.send(senha.encode("utf-8")[:4096])
        response = client.recv(4096)
        response = response.decode("utf-8")
        if response == "Logou!":
            return True
        else:
            return False
    except Exception as e:
        print(f"Erro no Login: {e}")
        return False
    finally:
        client.close()

def produto_get():
    try:
        client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server_ip = "localhost"
        server_port = 8083
        client.connect((server_ip, server_port))
        client.send("NULL".encode("utf-8")[:4096])
        loop = True
        lista = []
        while loop:
            response = client.recv(4096).decode("utf-8").strip("\x00")
            if response == "Acabou":
                client.send("-1".encode("utf-8")[:4096])
                loop = False
            else:
                lista.append(response)
        return lista
    except Exception as e:
        print(f"Erro no Produto: {e}")
    finally:
        client.close()
    return []

def produto_send(opcao, user):
    try:
        client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server_ip = "localhost"
        server_port = 8083
        client.connect((server_ip, server_port))
        if user != None:
            client.send(user.encode("utf-8")[:4096])
            loop = True
            while loop:
                response = client.recv(4096).decode("utf-8").strip("\x00")
                if response == "Acabou":
                    client.send(opcao.encode("utf-8")[:4096])
                    loop = False
    except Exception as e:
        print(f"Erro no Produto: {e}")
    finally:
        client.close()

def pagamento_get(user):
    try:
        client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server_ip = "localhost"
        server_port = 8084
        client.connect((server_ip, server_port))
        lista = []
        if user != None:
            client.send(user.encode("utf-8")[:4096])
            loop = True
            while loop:
                response = client.recv(4096).decode("utf-8").strip("\x00")
                if response == "FIM":
                    return lista
                split = response.split(":")
                lista.append(split)
        return lista
    except Exception as e:
        print(f"Erro no Pagamento_get: {e}")
    finally:
        client.close()

def pagamento_pay(user, saldo_add):
    try:
        client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server_ip = "localhost"
        server_port = 8084
        client.connect((server_ip, server_port))
        enviar = user + ":" + str(saldo_add)
        client.send(enviar.encode("utf-8"))
    except Exception as e:
        print(f"Erro no Pagamento_send: {e}")
    finally:
        client.close()