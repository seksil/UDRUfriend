package th.ac.udru.seksil;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    //    Explicit
    private Uri uri;
    private ImageView imageView;
    private boolean aBoolean = true;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Create Toolbar
        createToolbar();

//        Avata Controller
        avataController();


    }   // Main Method

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.itemUpload) {

            cheackData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void cheackData() {

        MyAlert myAlert = new MyAlert(getActivity());

//        Get Value From EditText to String
        EditText nameEditText = getView().findViewById(R.id.edtName);
        EditText emailEditText = getView().findViewById(R.id.edtEmail);
        EditText passwordEditText = getView().findViewById(R.id.edtPassword);
        EditText rePasswordEditText = getView().findViewById(R.id.edtRePassword);

        String nameString = nameEditText.getText().toString().trim();
        String emailString = emailEditText.getText().toString().trim();
        String passwordString = passwordEditText.getText().toString().trim();
        String rePasswordString = rePasswordEditText.getText().toString().trim();

        if (aBoolean) {
            myAlert.normalDialog("No Avata", "Please Choose Image for Avata");
        } else if (checkSpace(nameString, emailString, passwordString, rePasswordString)) {
            myAlert.normalDialog(getString(R.string.title_have_space), getString(R.string.message_have_space));
        } else if (passwordString.equals(rePasswordString)) {
//            Password Math
            uploadToFirebase(nameString, emailString, passwordString);
        } else {
            myAlert.normalDialog("Password Not Math", "Please Type Password agains");
        }

    }


    private void uploadToFirebase(final String nameString, final String emailString, final String passwordString) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please Wait...");
        progressDialog.show();

//        upload Image
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        final StorageReference storageReference1 = storageReference.child("Avata/" + nameString);
        storageReference1.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(getActivity(), "Success Upload", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

//                Find Path URL
                storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("21novV1", "PathURL ==> " + uri.toString());
                        registerFirebase(nameString, emailString, passwordString, uri.toString());
                    }
                });

            }   // onSuccess
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Cannot Upload", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });






    }   // upload

    private void registerFirebase(final String nameString,
                                  String emailString,
                                  String passwordString,
                                  final String pathUrlString) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseAuth firebaseAuth1 = FirebaseAuth.getInstance();
                            final FirebaseUser firebaseUser = firebaseAuth1.getCurrentUser();
                            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest
                                    .Builder().setDisplayName(nameString).build();
                            firebaseUser.updateProfile(userProfileChangeRequest)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("21novV1", "DisplayName ==> " + firebaseUser.getDisplayName());
                                            Log.d("21novV1", "userID ==> " + firebaseUser.getUid());
                                            createDatabase(firebaseUser.getUid(), pathUrlString, 17.397590, 102.794550, nameString);
                                        }
                                    });

                        } else {
                            MyAlert myAlert = new MyAlert(getActivity());
                            myAlert.normalDialog("Cannot Register", task.getException().toString());
                        }

                    }   // Complete
                });


    }   // register

    private void createDatabase(String uidString,
                                String pathUrlString,
                                double latDouble,
                                double lngDouble,
                                String nameString) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference()
                .child("User").child(uidString);

        DatabaseModel databaseModel = new DatabaseModel(uidString, pathUrlString, nameString, latDouble, lngDouble);

        databaseReference.setValue(databaseModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(getActivity(), ServiceActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });

    }   // createDatabase


    private boolean checkSpace(String nameString,
                               String emailString,
                               String passwordString,
                               String rePasswordString) {

        boolean result = false;

        if (nameString.isEmpty() || emailString.isEmpty() || passwordString.isEmpty() || rePasswordString.isEmpty()) {
            result = true;
        }

        return result;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_register, menu);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {

            uri = data.getData();
            aBoolean = false;

            try {

                Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, 800, 600, false);
                imageView.setImageBitmap(bitmap1);

            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            Toast.makeText(getActivity(),
                    "Please Choose Image",
                    Toast.LENGTH_SHORT).show();
        }

    }   // Result

    private void avataController() {
        imageView = getView().findViewById(R.id.imageViewAvatar);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,
                        "Please Choose App and Image"), 5);

            }
        });
    }

    private void createToolbar() {
        Toolbar toolbar = getView().findViewById(R.id.toolbarRegister);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.register);
        ((MainActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.message_have_space);
        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        setHasOptionsMenu(true);

    }

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

}