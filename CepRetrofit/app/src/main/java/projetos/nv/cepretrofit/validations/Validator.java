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

    public static boolean validateCPF(String cpf) {
        if(cpf.length() == 11){
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateCEP(String cep) {
        if(cep.length() == 8){
            return true;
        } else {
            return false;
        }
    }

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
