package br.edu.ifspsaocarlos.sdm.buscarmensagensvolleysdm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MostraMensagensActivity extends AppCompatActivity {
    // Referência para o ListView do Layout
    private ListView mostraMensagensLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_mensagens);

        // Buscando referência para o elemento de UI
        mostraMensagensLv = findViewById(R.id.lv_mostra_mensagens);

        // Recuperando o Array de mensagens que virá como parâmetro
        ArrayList<String> mensagensAl = getIntent().getStringArrayListExtra(MainActivity.MENSAGENS_STRING_ARRAY_EXTRA);

        // Criando e setando um Adapter para o ListView se existem Mensagens para mostrar
        if (mensagensAl != null && mensagensAl.size() > 0) {
            mostraMensagensLv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mensagensAl));
        }
        else {
            Toast.makeText(this, "Não há mensagens para esses parâmetros", Toast.LENGTH_LONG).show();
        }
    }
}
