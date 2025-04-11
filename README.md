Este projeto envolve a criação de um serviço básico de chat que pode ser usado entre diferentes máquinas (ou instâncias de uma aplicação entre uma mesma máquina), com o uso de criptografia AES (Advanced Encryption Standard) para a encriptação de ponta a ponta.
Para o funcionamento, no caso da conexão com outra máquina, o endereço IP deve ser inserido no arquivo "ChatView", e o arquivo de "ServidorMain" deve ser executado antes de qualquer cliente.

Inicialmente, a tela de conexão irá pedir um nome, além de uma chave para ser usada para a criptografia simétrica. Considere que apenas clientes com a mesma palavra-chave conseguirão se comunicar uns com os outros.

![image](https://github.com/user-attachments/assets/c5d10cac-03dd-48dc-b057-65295af49cae)

Na imagem abaixo, é possível ver a criptografia em efeito, com as mensagens entre "Alice" e "Bob" sendo encriptadas e decriptadas com a mesma chave, mas "Eve", que possui uma palavra-chave distinta, não consegue receber nem enviar mensagens para os demais usuários.

![image](https://github.com/user-attachments/assets/72c07c99-36a9-4367-a7f4-0ddfb739cb17)
