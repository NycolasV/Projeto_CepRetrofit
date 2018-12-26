package projetos.nv.cepretrofit.config;

import projetos.nv.cepretrofit.service.EnderecoService;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {

    private final Retrofit retrofit;
    private static final String BASE_URL = "http://viacep.com.br/ws/";

    // URL para conexão com a API e convertendo em JacksonFactory
    public RetrofitConfig() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    // Criando a interface para a utilização dos métodos
    public EnderecoService getEnderecoService(){
        return this.retrofit.create(EnderecoService.class);
    }
}
