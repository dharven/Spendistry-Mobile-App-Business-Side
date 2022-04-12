package com.shashank.spendistrybusiness.ViewModelFactory;

import android.app.Application;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.shashank.spendistrybusiness.ViewModels.DashboardViewModel;
import com.shashank.spendistrybusiness.ViewModels.InventoryViewModel;
import com.shashank.spendistrybusiness.ViewModels.InvoiceViewModel;
import com.shashank.spendistrybusiness.ViewModels.ReportViewModel;
import com.shashank.spendistrybusiness.ViewModels.ReturnedInvoicesViewModel;

@SuppressWarnings("ALL")
public class ViewModelFactory  extends androidx.lifecycle.ViewModelProvider.NewInstanceFactory{
    private final Object[] mParams;
    private final Application mApplication;

    public ViewModelFactory(Application application, Object... params) {
        mParams = params;
        mApplication = application;
    }

    @NonNull
    @Override
    public <T extends androidx.lifecycle.ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        if (modelClass.isAssignableFrom(ReturnedInvoicesViewModel.class)) {
            return (T) new ReturnedInvoicesViewModel(mApplication, (String) mParams[0]);
        }
        else if (modelClass.isAssignableFrom(InvoiceViewModel.class)) {
            return (T) new InvoiceViewModel(mApplication, (String) mParams[0]);
        }
        else if (modelClass.isAssignableFrom(InventoryViewModel.class)) {
            return (T) new InventoryViewModel(mApplication, (String) mParams[0]);
        }
        else if (modelClass.isAssignableFrom(ReportViewModel.class)) {
            return (T) new ReportViewModel(mApplication, (String) mParams[0]);
        }
        else if (modelClass.isAssignableFrom(DashboardViewModel.class)) {
            return (T) new DashboardViewModel(mApplication, (LinearLayout) mParams[0],(String) mParams[1]);
        }
        else {
            throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
        }
    }
}
