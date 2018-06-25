package cognitiva.dyslexreader;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    //Button to ReaderActivity
    Button btnReader;
    Button btnFile;
    Button btnPaste;
    Button btnCamera;
    Button btnSettings;

    TextView tvPreview;

    String currentAppTheme;


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
        setTheme(loadTheme());
        setContentView(R.layout.activity_main);


        /**
         * O textView será muito usado, então já coloca ele aqui
         */
        tvPreview = (TextView)findViewById(R.id.tvPreview);
        tvPreview.setMovementMethod(new ScrollingMovementMethod());

        setBackground();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    public void createToast(String text)
    {
        Toast t = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        TextView v = (TextView) t.getView().findViewById(android.R.id.message);
        v.setBackgroundColor(Color.TRANSPARENT);
        t.show();
    }

    public void setBackground()
    {
        if(currentAppTheme.equals(getString(R.string.themeValueLight)))
        {
            tvPreview.setBackgroundColor(getResources().getColor(R.color.colorTextView_light));
            //tvPreview.setBackgroundColor(getResources().getColor(R.color.colorTextSuffix_dark));
        }
        else if(currentAppTheme.equals(getString(R.string.themeValueDark)))
        {
            tvPreview.setBackgroundColor(getResources().getColor(R.color.colorTextView_dark));
        }
        else if (currentAppTheme.equals(getString(R.string.themeValueCustom)))
        {
            //TODO: Colocar o background custom aqui
            //tvPreview.setBackgroundColor(getResources().getColor(R.));
        }
    }

    /**
     * Usado para carregar o Tema custom pegando as cores das Preferences
     * @param preferences referencia ao SharedPreferences para carregar as cores
     */
    public static void loadCustomTheme(SharedPreferences preferences){
        //Color customBackground = preferences.get
    }

    public int loadTheme()
    {
        SharedPreferences preferences = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this);
        currentAppTheme = preferences.getString(getString(R.string.themeKey), getString(R.string.themeValueLight));
        preferences.registerOnSharedPreferenceChangeListener(this);
        String currentAppTheme = preferences.getString(getString(R.string.themeKey), getString(R.string.themeValueLight));
        if(currentAppTheme.equals(getString(R.string.themeValueLight)))
        {
            return R.style.AppTheme_Light;
        }
        else if(currentAppTheme.equals(getString(R.string.themeValueDark)))
        {
            return R.style.AppTheme_Dark;
        }
        else
        {
            //TODO: Colocar o tema custom
            return R.style.AppTheme_Dark;
        }

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
            createToast(getResources().getString(R.string.toastSendError));
            //makeText(this, R.string.toastSendError, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.themeKey)))
        {
            String mode = sharedPreferences.getString(key, getString(R.string.themeValueLight));
            if(mode.equals(getString(R.string.themeValueDark)))
            {
                currentAppTheme = getString(R.string.themeValueDark);
            }
            else if(mode.equals(getString(R.string.themeValueLight)))
            {
                currentAppTheme = getString(R.string.themeValueLight);
            }
            else if(mode.equals(getString(R.string.themeValueCustom)))
            {
                currentAppTheme = getString(R.string.themeValueCustom);
            }
            setTheme(loadTheme());
            setBackground();

            //Isso faz com que recarregue a interface corretamente, mas reseta a posição da palavra
            recreate();
        }
    }
}
