package com.josemillanes.covidassist;


//En esta clase están todos los métodos utilizados para conectarse con Firebase

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class FirebaseConnection {

      /////////////////////////////////////////////////////////////////////////////
     // A continuación se listan los métodos y propieadades sobre Firebase Auth //
    /////////////////////////////////////////////////////////////////////////////
    private static FirebaseAuth mAuth;

    //Este método se usa para inicializar la instancia de FirebaseAuth
    public void startFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
    }

    //Este método se usa para verificar que se inició sesión
    public boolean getCurrentUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null;
    }

    //Este método se usa para registrar un nuevo usuario
    public void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            //Qué hacer si se crea el usuario correctamente
                        } else {
                            //Qué hacer si no se crea el usuario correctamente
                        }
                    }
                });
    }

    //Este método se usa para iniciar sesión
    public void singIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            //Qué hacer si se inicia sesión correctamente
                        } else {
                            //Qué hacer si no se inicia sesión correctamente
                        }
                    }
                });
    }

    //Acceder a la info del usuario
    public FirebaseUser getUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
        return user;
    }

      //////////////////////////////////////////////////////////////////////////////////
     // A continuación se listan los métodos y propieadades sobre Firebase Firestore //
    //////////////////////////////////////////////////////////////////////////////////


    //Los siguientes dos métodos son necesarios para utilizar los datos de los usuarios de forma asincrona
    private void readUsersData(FirestoreCallback firestoreCallback, List<Usuario> usuarios) {
        db.collection("usuarios")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            //Qué hacer si la operación es exitosa
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("LIST",document.getData().toString());
                                Usuario usuario = new Usuario(
                                        document.getId(),
                                        document.getData().get("userName").toString(),
                                        document.getData().get("userEmail").toString(),
                                        document.getData().get("userImg").toString()
                                );
                                usuarios.add(usuario);
                            }
                            firestoreCallback.onCallback(usuarios);
                        } else {
                            //Qué hacer si la operación no es exitosa
                        }
                    }
                });
    }

    private interface FirestoreCallback {
        void onCallback(List<Usuario> list);
    }

    private static FirebaseFirestore db;

    //Este método se usa para inicializar la instancia de FirebaseFirestore
    public void startFirestore() {
        db = FirebaseFirestore.getInstance();
    }


    //Método para agregar usuarios a Firestore
    public void addUser(Map<String, Object> userMap) {
        db.collection("usuarios")
                .add(userMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Qué hacer si se agrega el usuario correctamente
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Qué hacer si no se agrega el usuario correctamente
                    }
                });
    }

    //Método para leer la colección de usuarios
    public List<Usuario> getUsers(TextView userNameText, TextView userEmailText, ImageView userImage) {
        List<Usuario> usuarios = new ArrayList<Usuario>();
        readUsersData(new FirestoreCallback() {
            @Override
            public void onCallback(List<Usuario> list) {
                //Do nothing?
                Log.d("TAG",list.toString());
                userNameText.setText(list.get(0).getUserName());
                userEmailText.setText(list.get(0).getUserEmail());
                //El siguiente método es necesario para obtener imágenes de internet y mostrarlas en imageview
                new DownloadImageTask(userImage).execute(list.get(0).getUserImg());
              /*  try {
                    URL url = new URL(list.get(0).getUserImg());
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    userImage.setImageBitmap(bmp);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
        }, usuarios);

        return usuarios;
    }

    //Método para actualizar un usuario
    public void updateUser(Map<String, Object> userMap, String userId) {
        DocumentReference docRef = db.collection("usuarios").document(userId);
        docRef.update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    //Qué hacer si se actualiza correctamente
                } else {
                    //Qué hacer si no se actualiza correctamente
                }
            }
        });
    }

    //Método para eliminar un usuario
    public void deleteUser (String userId) {
        db.collection("usuarios").document(userId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Qué hacer si se elimina correctamente
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Qué hacer si no se elimina correctamente
                    }
                });
    }

    //Método para agregar eventos a Firestore
    public void addEvent(Map<String, Object> eventMap) {
        db.collection("eventos")
                .add(eventMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Qué hacer si se agrega el usuario correctamente
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Qué hacer si no se agrega el usuario correctamente
                    }
                });
    }

    //Método para leer la colección de eventos
    public List<Evento> getEventos() {
        List<Evento> eventos = new ArrayList<Evento>();
        db.collection("eventos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            //Qué hacer si la operación es exitosa
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                Evento evento = new Evento(
                                        document.getId(),
                                        document.getData().get("eventTitle").toString(),
                                        document.getData().get("eventPlace").toString(),
                                        (Date)document.getData().get("eventDate"),
                                        document.getData().get("eventStatus").toString(),
                                        (int) document.getData().get("eventCapacity"),
                                        document.getData().get("eventCreator").toString(),
                                        (boolean) document.getData().get("eventContagio"),
                                        (List<Usuario>) document.getData().get("eventAttendance")
                                );
                                eventos.add(evento);
                            }
                        } else {
                            //Qué hacer si la operación no es exitosa
                        }
                    }
                });
        return eventos;
    }

    //Método para actualizar un evento
    public void updateEvent(Map<String, Object> eventMap, String eventId) {
        DocumentReference docRef = db.collection("eventos").document(eventId);
        docRef.update(eventMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    //Qué hacer si se actualiza correctamente
                } else {
                    //Qué hacer si no se actualiza correctamente
                }
            }
        });
    }

    //Método para eliminar un evento
    public void deleteEvent (String eventId) {
        db.collection("eventos").document(eventId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Qué hacer si se elimina correctamente
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Qué hacer si no se elimina correctamente
                    }
                });
    }

    //////////////////////////////////////////////////////////////////////////////
    // A continuación se listan los métodos y propieadades sobre Cloud Storage //
    ////////////////////////////////////////////////////////////////////////////

    private static FirebaseStorage storage;
    private static StorageReference storageRef;
    private static StorageReference usersRef;

    public void startStorage() {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        usersRef = storage.getReference();
    }

    //Método para subir imágenes a Firebase Storage usando un imageView
    public void uploadImageBytes(ImageView image) {
        image.setDrawingCacheEnabled(true);
        image.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = usersRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Qué hacer si falla la subida
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Qué hacer si se logra la subida
                //taskSnapshot.getMetadata() se usa para obtener metadatos
            }
        });
    }

    //Método para subir imágenes desde un archivo
    public void uploadImageFile(String filePath) {
        Uri file = Uri.fromFile(new File(filePath));
        StorageReference newFileRef = usersRef.child(file.getLastPathSegment());
        UploadTask uploadTask = newFileRef.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Qué hacer si falla la subida
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Qué hacer si se logra la subida
            }
        });
    }

    //Vienen más métodos en
    //https://firebase.google.com/docs/storage/android/upload-files?hl=es
    //pero la verdad es q me perdí


}
