package projetos.nv.cepretrofit.service;

import projetos.nv.cepretrofit.models.Endereco;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EnderecoService {

    @GET("{cep}/json")
    Call<Endereco> buscarEndereco(@Path("cep") String cep);
}
