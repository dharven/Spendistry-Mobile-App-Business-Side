package com.shashank.spendistrybusiness.ViewModels;

import android.app.Application;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.shashank.spendistrybusiness.Repository.InvoiceRepository;

public class ScanQrViewModel extends AndroidViewModel {
    InvoiceRepository invoiceRepository;

    public ScanQrViewModel(@NonNull Application application) {
        super(application);
        invoiceRepository = new InvoiceRepository(application);
    }

    public MutableLiveData<String> verifyQR(LinearLayout linearLayout, String QR){
        return invoiceRepository.verifyQR(linearLayout,QR);
    }
}
