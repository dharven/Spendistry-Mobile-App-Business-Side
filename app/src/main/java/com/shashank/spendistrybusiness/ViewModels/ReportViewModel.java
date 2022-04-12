package com.shashank.spendistrybusiness.ViewModels;

import android.app.Application;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.shashank.spendistrybusiness.Models.Report;
import com.shashank.spendistrybusiness.Models.User;
import com.shashank.spendistrybusiness.Repository.InvoiceRepository;

import java.util.List;

public class ReportViewModel extends AndroidViewModel {
    InvoiceRepository invoiceRepository;
    MutableLiveData<List<Report>> reportList;
    public ReportViewModel(@NonNull Application application, String email) {
        super(application);
        invoiceRepository = new InvoiceRepository(application);
        reportList = invoiceRepository.getReportedInvoices(email);

    }

    public void deleteReportRequest(LinearLayout linearLayout, String email) {
        invoiceRepository.deleteReportRequest(linearLayout, email);
    }

    public MutableLiveData<User> getUser(String email) {
        return invoiceRepository.getUser(email);
    }
    public MutableLiveData<List<Report>> getReportedInvoices(){
        return reportList;
    }
}
