package br.edu.ifspsaocarlos.sdm.buscarmensagensvolleysdm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // Referências para os elementos de UI
    private EditText remetenteEt;
    private EditText destinatarioEt;
    private EditText ultimaMensagemEt;

    // Constante para Web Services
    private final String URL_BASE = "http://www.nobile.pro.br/sdm/mensageiro/";
    private final String END_POINT = "mensagem";
    private final String ARRAY_MENSAGENS_JSON = "mensagens";
    private final String CORPO_MENSAGEM_JSON = "corpo";

    // Constante para passar como parâmetro para a tela que vai mostrar as mensagens
    public static final String MENSAGENS_STRING_ARRAY_EXTRA = "MENSAGENS_STRING_ARRAY_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Buscando referências para objetos de UI
        remetenteEt = findViewById(R.id.et_remetente);
        destinatarioEt = findViewById(R.id.et_destinatario);
        ultimaMensagemEt = findViewById(R.id.et_ultima_mensagem);
    }

    public void buscarMensagens(View view) {
        if (view.getId() == R.id.bt_buscar_mensagens) {
            // Monta a URL do serviço de busca de mensagens
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(URL_BASE + END_POINT);
            urlBuilder.append("/" + ultimaMensagemEt.getText().toString());
            urlBuilder.append("/" + remetenteEt.getText().toString());
            urlBuilder.append("/" + destinatarioEt.getText().toString());

            String url = urlBuilder.toString();

            // Cria uma fila de requisições Volley
            RequestQueue filaRequisicoes = Volley.newRequestQueue(this);

            /* Cria uma Requisição para um JsonObject usando o método GET. Como estamos usando
            * GET e não POST, o jsonRequest (terceiro parâmetro) vai null. Os Listeners são
            * usados para tratar a resposta da requisição*/
            JsonObjectRequest buscaMensagensJor = new JsonObjectRequest(Request.Method.GET, url, null, new ResponseListener(), new ErrorListener());

            // Adiciona a Requisição à Fila de Requisições Volley
            filaRequisicoes.add(buscaMensagensJor);

            // Limpa os campos
            limparCampos();
        }
    }

    private void limparCampos() {
        remetenteEt.setText("");
        destinatarioEt.setText("");
        ultimaMensagemEt.setText("");
    }

    private class ResponseListener implements Response.Listener<JSONObject>{
        // Executado no caso de uma resposta
        @Override
        public void onResponse(JSONObject response) {
            try {
                // Recupera o JSONArray que veio na resposta
                JSONArray mensagensJa = response.getJSONArray(ARRAY_MENSAGENS_JSON);

                // Cria um ArrayList de Strings para guardar o CORPO das mensagens
                ArrayList<String> mensagensAl = new ArrayList<>();

                // Percorre o JSONArray para recuperar o CORPO das mensagens
                for (int indice = 0; indice < mensagensJa.length(); indice++) {
                    // Em cada posição do JSONArray recupera tem um JSONObject
                    JSONObject mensagemJson = (JSONObject) mensagensJa.get(indice);
                    // Recupera o CORPO da mensagem de dentro do JSONObject
                    mensagensAl.add(mensagemJson.getString(CORPO_MENSAGEM_JSON));
                }

                /* Agora com o ArrayList de Strings, cria uma Intent para enviar mostrar essas
                * mensagens*/
                Intent mostrarMensagensIntent = new Intent(getApplicationContext(), MostraMensagensActivity.class);

                // Passa o ArrayList de String como EXTRA para a Intent
                mostrarMensagensIntent.putStringArrayListExtra(MENSAGENS_STRING_ARRAY_EXTRA, mensagensAl);

                // Inicia a Activity que vai mostrar as mensagens
                startActivity(mostrarMensagensIntent);
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Erro na conversão da resposta", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    private class ErrorListener implements Response.ErrorListener {
        // Executado no caso de um erro
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(), "Erro na resposta do WS", Toast.LENGTH_LONG).show();
        }
    }

}
