package com.example.together.Group;
// 그룹 상세보기 화면

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.together.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class look_group extends AppCompatActivity {
    private Intent intent;
    private String Gname, master, uname, announce;

    // 2는 그룹장이 공지사항을 눌렀을 경우, 기본은 일반 그룹원이 볼 경우
    Dialog announceDialog, announceDialog2;
    ImageButton back, setting;
    TextView gname, dia_content;
    EditText dia_content2;
    Button announce_btn, chat_btn;


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<User_group> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid(); //유저 아이디

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_group);

        intent = getIntent();
        Gname = intent.getStringExtra("Gname");
        uname = intent.getStringExtra("uname");

        announceDialog = new Dialog(this);
        announceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//제목 제거
        announceDialog.setContentView(R.layout.one_button_dialog);
        announceDialog2 = new Dialog(this);
        announceDialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);//제목 제거
        announceDialog2.setContentView(R.layout.edit_two_btn_dialog);

        dia_content = (TextView) announceDialog.findViewById(R.id.dia_content);
        dia_content2 = (EditText) announceDialog2.findViewById(R.id.dia_content);

        //변수들 레이아웃 id값이랑 연결
        back = (ImageButton) findViewById(R.id.back);
        gname = (TextView) findViewById(R.id.gname);
        setting = (ImageButton) findViewById(R.id.setting);
        announce_btn = (Button) findViewById(R.id.announce);
        chat_btn = (Button) findViewById(R.id.chat_btn);


        gname.setText(Gname); //그룹명 연결


        //커스텀 리스트뷰 시작
        recyclerView = findViewById(R.id.recyclerView); // 아디 연결
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // 그룹 객체를 담을 어레이 리스트 (어댑터쪽으로)

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

        databaseReference = database.getReference(); // DB 테이블 연결

        // 본인의 그룹장 여부를 어댑터로 보내기 위한 코드
        databaseReference.child("User").child(uid).child("Group").child(Gname).child("master").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                master = dataSnapshot.getValue(String.class);

                // 그룹원 리스트를 출력
                databaseReference.child("Together_group_list").child(Gname).child("user").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                        arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                            User_group User_group = snapshot.getValue(User_group.class); // 만들어뒀던 Glook_list 객체에 데이터를 담는다.
                            arrayList.add(User_group); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                        }
                        adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // 디비를 가져오던중 에러 발생 시
                        //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                    }

                });

                adapter = new Glook_Adapter(arrayList, Gname, master, uname, look_group.this); // 그룹이름과 마스터정보를 넘김
                recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        // 그룹 공지사항 가져오기
        databaseReference.child("Together_group_list").child(Gname).child("announce").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                announce = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }

        });

        // 공지사항 버튼을 눌렀을 경우
        announce_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (master.equals("yes")) {
                    showDialog2(Gname);
                    dia_content2.setText(announce); // 그룹장은 수정가능한 공지사항 화면
                } else {
                    showDialog(Gname);
                    dia_content.setText(announce); // 일반 그룹원은 수정 불가능한 공지사항 화면
                }
            }
        });

        // 설정 버튼을 눌렀을 경우
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Group_setting.class); // 그룹 상세 화면으로 연결
                intent.putExtra("Gname", Gname); // 그룹 이름 넘기기
                intent.putExtra("master", master); // 본인의 마스터 정보를 넘기기
                startActivity(intent); // 액티비티 열기
            }
        });

        // 채팅 버튼을 눌렀을 경우
        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), chat_room.class); // 채팅 화면으로 이동
                intent.putExtra("Gname", Gname); // 그룹 이름 넘겨기기
                intent.putExtra("uname", uname); // 사용자 닉네임 넘기기
                intent.putExtra("master", master); // 본인의 마스터 정보를 넘기기
                startActivity(intent); // 액티비티 열기
            }
        });


        //뒤로가기 버튼
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    // 그룹원이 공지사항 보는 다이얼로그
    public void showDialog(String Gname) {
        announceDialog.show(); // 다이얼로그 출력
        announceDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 끝부분을 둥굴게 하기 위해 투명색 지정
        Button yesBtn = announceDialog.findViewById(R.id.yesBtn); // 확인 버튼

        //확인 버튼
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                announceDialog.dismiss(); // 다이얼로그 닫기
            }
        });

    }

    // 그룹장이 공지사항 보고 수정하는 다이얼로그
    public void showDialog2(String Gname) {
        announceDialog2.show(); // 다이얼로그 출력
        announceDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 끝부분을 둥굴게 하기 위해 투명색 지정
        Button yesBtn = announceDialog2.findViewById(R.id.yesBtn); // 확인 버튼
        Button noBtn = announceDialog2.findViewById(R.id.noBtn); // 취소 버튼

        // 닫기 버튼
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                announceDialog2.dismiss(); // 다이얼로그 닫기
            }
        });

        // 확인 버튼
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 수정된 공지사항 반영
                databaseReference.child("Together_group_list").child(Gname).child("announce").setValue(dia_content2.getText().toString());
                announceDialog2.dismiss(); // 다이얼로그 닫기
            }
        });

    }
}
