package com.muthoknee.morningfirebasedbapp

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    lateinit var edtName:EditText
    lateinit var edtEmail:EditText
    lateinit var edtIdNumber:EditText
    lateinit var btnSave: Button
    lateinit var btnView:Button
    lateinit var spinnerOptions:Spinner
    lateinit var progressDialog: ProgressDialog
    lateinit var optionSelected:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtName = findViewById(R.id.mEdtName)
        edtEmail = findViewById(R.id.mEdtEmail)
        edtIdNumber = findViewById(R.id.mEdtIdNumber)
        btnSave = findViewById(R.id.mBtnSave)
        btnView = findViewById(R.id.mBtnView)
        spinnerOptions = findViewById(R.id.mSpinerOptions)
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Saving")
        progressDialog.setMessage("Please wait...")
        spinnerOptions.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                optionSelected = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        btnSave.setOnClickListener{
            var name = edtName.text.toString().trim()
            var email = edtEmail.text.toString().trim()
            var idNumber = edtIdNumber.text.toString().trim()
            var id = System.currentTimeMillis().toString()

            if (name.isEmpty()){
                edtName.setError("Please fill this field")
                edtName.requestFocus()
            }else if (email.isEmpty()){
                edtEmail.setError("Please fill this field")
                edtEmail.requestFocus()
            }else if (idNumber.isEmpty()){
                edtIdNumber.setError("Please fill this field")
                edtIdNumber.requestFocus()
            }else{
                //Proceed to save
                //Prepare the user to be saved
                var user = User(name, email, idNumber, id, optionSelected)
                //Create a reference in the firebase database
                var ref = FirebaseDatabase.getInstance().getReference().child("Users/"+id)

                //Show the progress to start saving
                progressDialog.show()
                ref.setValue(user).addOnCompleteListener{
                    //Dismiss the progress and check if the task was successful
                    task ->
                    progressDialog.dismiss()
                    if (task.isSuccessful){
                        Toast.makeText(this,"User saved successfully", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this, "User saving failed", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        btnView.setOnClickListener{

        }
    }
}