import socket

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_ip = "localhost"
server_port = 8000
client.connect((server_ip, server_port))

while True:
    print("0 - Sair")
    print("1 - Cadastro")
    print("2 - Login")
    print("3 - produtos")
    print("4 - pagamentos")
    try:
        opcao = int(input("Opção: "))
    except:
        print("ERROR")
        continue
    if opcao == 1:
        print('Cadastro')
    elif opcao == 2:
        print('Login')
    elif opcao == 3:
        print('Produtos')
    elif opcao == 4:
        print('Pagamento')
    elif opcao == 0:
        print('Sair')
        break
    else:
        print("Insira uma opcao valida")
