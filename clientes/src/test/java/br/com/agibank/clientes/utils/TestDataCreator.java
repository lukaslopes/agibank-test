package br.com.agibank.clientes.utils;

import br.com.agibank.clientes.api.model.ClienteModel;
import br.com.agibank.clientes.api.model.EnderecoModel;
import br.com.agibank.clientes.domain.model.Cliente;
import br.com.agibank.clientes.domain.model.Endereco;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class TestDataCreator {

    public static final UUID CLIENTE_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    public static final String CPF = "12345678901";
    public static final String NOME = "João da Silva";
    public static final LocalDate DATA_NASCIMENTO = LocalDate.of(1990, 1, 1);
    public static final String TELEFONE = "11999999999";

    public static Cliente createCliente() {
        Cliente cliente = new Cliente();
        cliente.setCpf(CPF);
        cliente.setNome(NOME);
        cliente.setDataNascimento(DATA_NASCIMENTO);
        cliente.setTelefone(TELEFONE);
        cliente.setEndereco(createEndereco());
        return cliente;
    }

    public static Endereco createEndereco() {
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua das Flores");
        endereco.setNumero("123");
        endereco.setComplemento("Apto 101");
        endereco.setBairro("Centro");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setCep("01001000");
        return endereco;
    }

    public static ClienteModel createClienteModel(Cliente cliente) {
        ClienteModel model = new ClienteModel();
        model.setId(cliente.getId());
        model.setCpf(cliente.getCpf());
        model.setNome(cliente.getNome());
        model.setDataNascimento(cliente.getDataNascimento());
        model.setTelefone(cliente.getTelefone());
        model.setEndereco(createEnderecoModel(cliente.getEndereco()));
        return model;
    }

    public static List<ClienteModel> createClienteModelList() {
        Cliente cliente = createCliente();
        cliente.setId(CLIENTE_ID);
        return List.of(createClienteModel(cliente));
    }

    private static EnderecoModel createEnderecoModel(Endereco endereco) {
        if (endereco == null) {
            return null;
        }
        EnderecoModel model = new EnderecoModel();
        model.setLogradouro(endereco.getLogradouro());
        model.setNumero(endereco.getNumero());
        model.setComplemento(endereco.getComplemento());
        model.setBairro(endereco.getBairro());
        model.setCidade(endereco.getCidade());
        model.setEstado(endereco.getEstado());
        model.setCep(endereco.getCep());
        return model;
    }
}
