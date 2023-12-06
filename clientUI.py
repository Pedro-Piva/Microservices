import PySimpleGUI as sg
from runclient import cadastro, login, produto_get, produto_send, pagamento_get, pagamento_pay

global user
user = None
sg.theme("DarkAmber")

layout = [
    [
        sg.Text(
            "User: ", justification="center", font=("Arial Bold", 30), key="principal"
        )
    ],
    [sg.Button(button_text="Cadastro", size=(30, 3), font=("arial bold", 15))],
    [sg.Button(button_text="Login", size=(30, 3), font=("arial bold", 15))],
    [sg.Button(button_text="Produtos", size=(30, 3), font=("arial bold", 15))],
    [sg.Button(button_text="Pagamento", size=(30, 3), font=("arial bold", 15))],
    [
        sg.Button(
            button_text="Sair",
            size=(30, 3),
            button_color="#FF3F3F",
            font=("arial bold", 15),
        )
    ],
]
main_window = sg.Window("Lojinha", layout, margins=(300, 100))


def cadastro_ui():
    main_window.disable()
    main_window.minimize()
    layout_cadastro = [
        [sg.Text("CADASTRO", justification="center", font=("Arial Bold", 30))],
        [sg.Push(), sg.Text("Nome"), sg.Input(key="nome")],
        [sg.Push(), sg.Text("Login"), sg.Input(key="login")],
        [sg.Push(), sg.Text("Senha"), sg.Input(key="senha")],
        [sg.Push(), sg.Text("Idade"), sg.Input(key="idade")],
        [sg.Push(), sg.Text("Saldo"), sg.Input(key="saldo")],
        [sg.Button("Sair"), sg.Push(), sg.Button("Enviar")],
    ]
    cadastro_window = sg.Window("cadastro", layout_cadastro, margins=(300, 100))
    while True:
        event_c, values_c = cadastro_window.read()
        if event_c == "Sair" or event_c == sg.WIN_CLOSED:
            cadastro_window.close()
            break
        elif event_c == "Enviar":
            try:
                saldo = float(values_c["saldo"])
                idade = int(values_c["idade"])
                if cadastro(
                    values_c["nome"],
                    values_c["login"],
                    values_c["senha"],
                    values_c["idade"],
                    values_c["saldo"],
                ):
                    sg.popup(f"Cadastro concluido!", title="Result")
                    cadastro_window.close()
                    break
                else:
                    sg.popup(f"Login Existente", title="Result")
            except ValueError:
                sg.popup_error("Apenas numeros em idade e Saldo")
    main_window.enable()
    main_window.normal()


def login_ui():
    main_window.disable()
    main_window.minimize()
    layout_login = [
        [sg.Text("LOGIN", justification="center", font=("Arial Bold", 30))],
        [sg.Push(), sg.Text("Usuario"), sg.Input(key="login")],
        [sg.Push(), sg.Text("Senha"), sg.Input(key="senha")],
        [sg.Button("Sair"), sg.Push(), sg.Button("Enviar")],
    ]
    login_window = sg.Window("Login", layout_login, margins=(200, 100))
    while True:
        event_l, values_l = login_window.read()
        if event_l == "Sair" or event_l == sg.WIN_CLOSED:
            login_window.close()
            break
        elif event_l == "Enviar":
            if login(values_l["login"], values_l["senha"]):
                global user
                user = values_l["login"]
                sg.popup(f"Login concluido!", title="Result")
                login_window.close()
                main_window["principal"].update("User: " + user)
                break
            else:
                sg.popup(f"Usuario ou senha invalidos", title="Result")
    main_window.enable()
    main_window.normal()


def produtos_ui():
    main_window.disable()
    main_window.minimize()
    layout_produtos = []
    global user
    if user == None:
        layout_produtos = [
            [sg.Text("Produtos", justification="center", font=("Arial Bold", 30))],
            [
                sg.Button("Sair"),
                sg.Push(),
                sg.Button("Escolher", visible=False),
            ],
        ]
    else:
        layout_produtos = [
            [sg.Text("Produtos", justification="center", font=("Arial Bold", 30))],
            [
                sg.Button("Sair"),
                sg.Push(),
                sg.Button("Escolher", visible=True),
            ],
        ]
    lista = produto_get()
    count = 1
    valores = []

    for produto in lista:
        texto = produto.split(";")
        layout_produtos.insert(
            -1,
            [
                sg.Text(
                    f"{texto[0]} - {texto[1]} R$ {texto[2]}",
                    size=(30, 2),
                    key=str(count),
                ),
                sg.Push(),
            ],
        )
        valores.append(texto[1])
        count += 1
    layout_produtos.insert(
        -1, [sg.Text("Produto Escolhido: "), sg.InputCombo(valores, key="combo")]
    )

    produtos_window = sg.Window("Login", layout_produtos, margins=(300, 200))

    while True:
        event_p, values_p = produtos_window.read()
        if event_p == "Sair" or event_p == sg.WIN_CLOSED:
            produtos_window.close()
            break
        elif event_p == "Escolher":
            if values_p["combo"] in valores:
                produto_send(str(valores.index(values_p["combo"])), user)
                sg.popup(
                    "Produto: " + values_p["combo"] + ". Adicionado a Lista",
                    title="Result",
                )
                produtos_window.close()
                break
            else:
                sg.popup("Selecione uma opcao!")
    main_window.enable()
    main_window.normal()


def pagamento_ui():
    main_window.disable()
    main_window.minimize()
    layout_pagamento = []
    global user
    senha = ""
    saldo = 0
    if user != None:
        layout_pagamento = [
            [sg.Text("PAGAMENTO", justification="center", font=("Arial Bold", 30))],
            [sg.Push(), sg.Text("Adicionar Saldo"), sg.Input(key="addSaldo")],
            [sg.Push(), sg.Text("Senha"), sg.Input(key="senha")],
            [sg.Button("Sair"), sg.Push(), sg.Button("Pagar")],
        ]
        lista = pagamento_get(user)

        total = 0

        count = 0

        for i in lista:
            if count == 0:
                print(i)
                item = [sg.Text(f"User: {i[0]}, Saldo R${i[1]}")]
                saldo = float(i[1])
                senha = i[2]
                layout_pagamento.insert(1, item)
            else:
                print(i)
                item = [sg.Text(f"{i[0]} Preco: {i[1]}, Quantidade: {i[2]}")]
                total += float(i[1]) * int(i[2])
                layout_pagamento.insert(2, item)
            count += 1
        item = [sg.Text(f"Valor Total: {total}")]
        layout_pagamento.insert(-3, item)
    else:
        layout_pagamento = [
            [sg.Text("PAGAMENTO", justification="center", font=("Arial Bold", 30))],
            [sg.Button("Sair"), sg.Push()],
        ]
    pagamento_window = sg.Window("Login", layout_pagamento, margins=(300, 200))
    while True:
        event_p, values_p = pagamento_window.read()
        if event_p == "Sair" or event_p == sg.WIN_CLOSED:
            pagamento_window.close()
            break
        elif event_p == "Pagar":
            if values_p["senha"] == senha:
                if values_p["addSaldo"] != "":
                    if (saldo + float(values_p["addSaldo"])) >= total:
                        pagamento_pay(user, saldo + float(values_p["addSaldo"]))
                        sg.popup("Pagamento Concluido")
                    else:
                        sg.popup("SALDO INSUFICIENTE")
                else:
                    if saldo >= total:
                        pagamento_pay(user, saldo)
                        sg.popup("Pagamento Concluido")
                    else:
                        sg.popup("SALDO INSUFICIENTE")
                pagamento_window.close()
                break
            else:
                sg.popup("SENHA INCORRETA")
    main_window.enable()
    main_window.normal()


while True:
    event, values = main_window.read()
    if event == "Sair" or event == sg.WIN_CLOSED:
        break
    elif event == "Cadastro":
        cadastro_ui()
        print("Cadastro")
    elif event == "Login":
        login_ui()
        print("Login")
    elif event == "Produtos":
        produtos_ui()
        print("Produtos")
    elif event == "Pagamento":
        pagamento_ui()
        print("Pagamento")
