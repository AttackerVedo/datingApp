package com.attackervedo.myapplication.auth

import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.attackervedo.myapplication.MainActivity
import com.attackervedo.myapplication.R
import com.attackervedo.myapplication.utils.FirebaseRef
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream

class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    private var gender = ""
    private var location = ""
    private var age = ""
    private var nickname = ""
    private var uid = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        auth = Firebase.auth

        val joinBtn =findViewById<Button>(R.id.joinJoinBtn)
        val joinImage = findViewById<ImageView>(R.id.joinImage)
        //핸드폰에있는 갤러리로가는 액션
        val getAction = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {uri ->
                joinImage.setImageURI(uri)
            }
        )
        joinImage.setOnClickListener {
            getAction.launch("image/*")

        }

        joinBtn.setOnClickListener {
            joinBtnClick()
        }


    }//onCreate

    fun getGender(selectRadioId:Int):String{
        var result =""
        if(selectRadioId == -1){
            //no check
//            Toast.makeText(this, "성별을 체크해주세요.", Toast.LENGTH_SHORT).show()
            result = ""
        }else{
            // checked
            var selecedRadioButton = findViewById<RadioButton>(selectRadioId)
            var selectedText = selecedRadioButton.text.toString()

            when(selectedText){
                "남자" -> result = "남성"
                else -> result = "여성"
            }
        }
        return result
    }

    private fun joinBtnClick(){
        val joinImage = findViewById<ImageView>(R.id.joinImage)
        val joinRadioGroup = findViewById<RadioGroup>(R.id.joinRadioGroup)
        val email = findViewById<TextInputEditText>(R.id.joinEmail).text.toString()
        val password = findViewById<TextInputEditText>(R.id.joinPassword).text.toString()
        val passwordCheck = findViewById<TextInputEditText>(R.id.joinPasswordCheck).text.toString()

        //닉네임, 성별, 지역, 나이, uid
        // 성별 가져오기
        var selectRadioId = joinRadioGroup.checkedRadioButtonId

//            Log.e("GenderValue", gender)
        // 닉네임 , 지역 , 나이 , uid 가져오기
        nickname = findViewById<TextInputEditText>(R.id.joinNickName).text.toString()
        location = findViewById<TextInputEditText>(R.id.joinLocation).text.toString()
        age = findViewById<TextInputEditText>(R.id.joinAge).text.toString()
        gender = getGender(selectRadioId)

        // null check

        if(email == "")
            Toast.makeText(this, "이메일 입력해 주세요.", Toast.LENGTH_SHORT).show()
        else if(password == "")
            Toast.makeText(this, "비밀번호 입력해 주세요.", Toast.LENGTH_SHORT).show()
        else if(password.length <= 6)
            Toast.makeText(this, "비밀번호는 6자리 이상 이여야 합니다.", Toast.LENGTH_SHORT).show()
        else if(gender == "")
            Toast.makeText(this, "성별을 체크해 주세요.", Toast.LENGTH_SHORT).show()
        else if(location == "")
            Toast.makeText(this, "지역 입력해 주세요.", Toast.LENGTH_SHORT).show()
        else if(nickname == "")
            Toast.makeText(this, "닉네임 입력해 주세요.", Toast.LENGTH_SHORT).show()
        else if(age == "")
            Toast.makeText(this, "나이 입력해 주세요.", Toast.LENGTH_SHORT).show()
        else if(!password.equals(passwordCheck))
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
        else{
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    uid = user?.uid.toString()
                    val userModel = UserData(
                        uid,
                        nickname,
                        age,
                        gender,
                        location
                    )

                    FirebaseRef.userInfoRef.child(uid).setValue(userModel)
                    uploadImage(uid)

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                }
            }
        }
    }

    private fun uploadImage(uid:String){

        val imageView = findViewById<ImageView>(R.id.joinImage)
        val storage = Firebase.storage
        val storageRef = storage.reference.child(uid+".png")

        // Get the data from an ImageView as bytes
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }

    }
}