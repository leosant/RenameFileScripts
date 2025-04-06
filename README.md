# 📄 Manual de Uso - Renomeador de Comprovantes

![Banner](docs/banner-renomeador.gif) <!-- Sugestão: GIF animado do app renomeando arquivos -->

## 🧠 Sobre o Projeto

Este projeto tem como objetivo renomear automaticamente arquivos de comprovantes, sejam imagens ou PDFs, com base no conteúdo textual extraído desses arquivos.

> 💡 O projeto utiliza OCR (Tesseract) e leitura de PDF para extrair o texto dos arquivos, facilitando a organização e padronização dos nomes dos comprovantes.

---

## ⚙️ Como funciona o reconhecimento (versão 1.1.0)

A aplicação realiza uma análise do texto presente nos arquivos, procurando por palavras-chave específicas.

Na versão atual, as palavras utilizadas para _match_ de texto são:

- `para`
- `favorec`

Se uma dessas palavras for encontrada, o sistema tentará extrair a informação relevante que vem logo ao lado da palavra, e usará essa informação para renomear o arquivo.

📷 Exemplo de comprovante processado:

![Exemplo de Comprovante](docs/exemplo-comprovante.png)

---

## ▶️ Como utilizar

1. Crie a pasta de entrada no seguinte caminho:

   ```bash
   {home.do.usuário}/temp/renameScript/comprovantes

## 👨‍💻 Autor

Desenvolvido por **[@leosant](https://github.com/leosant)** com o objetivo de automatizar tarefas do dia a dia com OCR.
