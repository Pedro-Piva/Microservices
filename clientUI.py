import PySimpleGUI as sg
from runclient import cadastro, login, produto_get, produto_send

global user
user = None
sg.theme("DarkAmber")

layout = [
    [sg.Button(button_text="Cadastro", size=(30, 3))],
    [sg.Button(button_text="Login", size=(30, 3))],
    [sg.Button(button_text="Produtos", size=(30, 3))],
    [sg.Button(button_text="Pagamento", size=(30, 3))],
    [sg.Button(button_text="Sair", size=(30, 3), button_color="#FF3F3F")],
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
                break
            else:
                sg.popup(f"Usuario ou senha invalidos", title="Result")
    main_window.enable()
    main_window.normal()


def produtos_ui():
    main_window.disable()
    main_window.minimize()
    layout_produtos = [
        [sg.Text("Produtos", justification="center", font=("Arial Bold", 30))],
        [sg.Button("Sair"), sg.Push(), sg.Button("Escolher")],
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

    login_window = sg.Window("Login", layout_produtos, margins=(300, 200))

    while True:
        event_p, values_p = login_window.read()
        if event_p == "Sair" or event_p == sg.WIN_CLOSED:
            login_window.close()
            break
        elif event_p == "Escolher":
            if values_p["combo"] in valores:
                global user
                produto_send(str(valores.index(values_p["combo"])), user)
                sg.popup("Produto: " + values_p["combo"] + ". Adicionado a Lista", title="Result")
            else:
                sg.popup("Selecione uma opcao!")
    main_window.enable()
    main_window.normal()

def pagamento_ui():
    main_window.disable()
    main_window.minimize()
    layout_pagamento = [
        [sg.Text("PAGAMENTO", justification="center", font=("Arial Bold", 30))],
        [sg.Push(), sg.Text("Usuario"), sg.Input(key="login")],
        [sg.Push(), sg.Text("Senha"), sg.Input(key="senha")],
        [sg.Button("Sair"), sg.Push(), sg.Button("Enviar")],
    ]
    pagamento_window = sg.Window("Login", layout_pagamento, margins=(300, 200))
    while True:
        event_p, values_p = pagamento_window.read()
        if event_p == "Sair" or event_p == sg.WIN_CLOSED:
            pagamento_window.close()
            break
        elif event_p == "Enviar":
            break
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
