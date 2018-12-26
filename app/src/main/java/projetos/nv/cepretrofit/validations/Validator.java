package projetos.nv.cepretrofit.validations;

import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Validator {

    /**
     * Método de validação para conferir se o objeto recebido é nulo
     */
    public static boolean validateNotNull(View pView) {
        if (pView instanceof EditText) {
            EditText edText = (EditText) pView;
            Editable text = edText.getText();
            if (text != null) {
                String strText = text.toString();
                if (!TextUtils.isEmpty(strText)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * Método de validação para conferir se o CPF possui o tamanho correto de 11 dígitos
     */
    public static boolean validateCPF(String cpf) {
        if(cpf.length() == 11){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Método de validação para conferir se o CEP possui o tamanho correto de 8 dígitos
     */
    public static boolean validateCEP(String cep) {
        if(cep.length() == 8){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Método de validação para conferir a data de nascimento
     * Caso a data inserida seja maior que a data atual, envia um valor falso
     */
    public static boolean validateDataNascimento(String dataNascimento){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dataAtual = new Date();
            Date data = format.parse(dataNascimento);

            int dataAtualInt = (int) (dataAtual.getTime()/1000);
            int dataInt = (int) (data.getTime()/1000);

            if (dataInt > dataAtualInt){
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
