package cognitiva.dyslexreader;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Button to ReaderActivity
    Button btnReader;
    Button btnFile;
    Button btnPaste;
    Button btnCamera;
    Button btnSettings;

    TextView tvPreview;


    /***
     * TODO: Declaration of Parser Classes
     *
     * ImageParser imageParser
     * HTTPParser httpParser
     * EPUBParser epubParser
     * PDFParser pdfParser
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * O textView será muito usado, então já coloca ele aqui
         */
        tvPreview = (TextView)findViewById(R.id.tvPreview);
        tvPreview.setMovementMethod(new ScrollingMovementMethod());
    }


    /***
     * Button Callback for btnReader
     * Pega a string da caixa e manda pra nova Activity
     */
    public void onClickReader(View v){
        String text;
        text = tvPreview.getText().toString();
        if (text != "")
        {
            Intent intent = new Intent(getBaseContext(), ReaderActivity.class);
            intent.putExtra("text", text);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, R.string.toastSendError, Toast.LENGTH_SHORT).show();
        }

    }

    /***
     * Button Callback for btnFile
     * Abre o gerenciador de arquivos, mas ainda não faz nada se selecionar o arquivo
     */
    public void onClickFile(View v){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivity(intent);

    }

    /***
     * Button Callback for btnPaste
     * Pega o texto do clip board. Se tiver algo, coloca na caixa
     * Caso contrário, faz nada
     */
    public void onClickPaste(View v){
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if(clipboardManager.hasPrimaryClip())
        {
            ClipData.Item item = clipboardManager.getPrimaryClip().getItemAt(0);
            if (item.getText() != null)
            {
                tvPreview.setText(item.getText());
            }
            else
            {
                return;
            }
        }
    }

    /**
     *
     */
    public void onClickCamera(View v){}

    /***
     * Button Callback for btnSettings
     */
    public void onClickSettings(View v){
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    }
}
