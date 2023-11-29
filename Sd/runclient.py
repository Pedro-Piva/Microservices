import socket


def run_client():
    # create a socket object
    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    server_ip = "localhost"  # replace with the server's IP address
    server_port = 8081  # replace with the server's port number
    # establish connection with server
    client.connect((server_ip, server_port))
    print(client.getsockname())

    try:
        #response = "Enter message: "
        while True:
            response = client.recv(4096)
            response = response.decode("utf-8")

            msg = input(response)
            client.send(msg.encode("utf-8")[:4096])

            if response == "Informe uma Senha Valida: ":
                print("Cadastro Finalizado")
                break

    except Exception as e:
        print(f"Error: {e}")
    finally:
        client.close()
        print("Connection to server closed")


run_client()
