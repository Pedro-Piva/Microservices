import socket


def run_client():
    # create a socket object
    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    server_ip = "localhost"
    server_port = 8082

    client.connect((server_ip, server_port))
    print(client.getsockname())

    try:
        while True:
            response = client.recv(4096)
            response = response.decode("utf-8")

            if response == "Logou!":
                print("Login Finalizado")
                break

            msg = input(response)
            client.send(msg.encode("utf-8")[:4096])
    except Exception as e:
        print(f"Error: {e}")
    finally:
        client.close()
        print("Connection to server closed")


run_client()
