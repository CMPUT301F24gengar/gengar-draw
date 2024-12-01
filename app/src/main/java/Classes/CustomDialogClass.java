package Classes;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.example.gengardraw.R;

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public FrameLayout yes, no;

    public CustomDialogClass(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        yes = findViewById(R.id.proceed_delete_btn);
        no = findViewById(R.id.cancel_delete_btn);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    public interface DialogListener {
        void onProceed();
        void onCancel();
    }

    private DialogListener listener;

    public void setDialogListener(DialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.proceed_delete_btn) {
            if (listener != null) listener.onProceed();
            dismiss();
        } else if (view.getId() == R.id.cancel_delete_btn) {
            if (listener != null) listener.onCancel();
            dismiss();
        }
    }

}
