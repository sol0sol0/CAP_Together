package com.example.together.Group;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.together.Calendar.Calendar_note;
import com.example.together.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

//사진 관련 부분은 일단 주석처리 했습니다. 굳이 그룹에서 데이터베이스 연동할 이유 없어보여서요.
public class Search_Adapter extends RecyclerView.Adapter<Search_Adapter.CustomViewHoler> {

    private ArrayList<Together_group_list> arrayList;
    private Context context;

    Dialog PlanDialog;//그룹 가입을 위한 Dialog
    TextView dia_content; //다이얼로그 내용
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;


    public Search_Adapter(ArrayList<Together_group_list> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.together_list_item, parent, false);
        CustomViewHoler holer = new CustomViewHoler(view);
        return holer;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHoler holder, int position) { //사진 만드는 그런것..
        /*
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getiv_people())
                .into(holder.iv_people); */

        //다이얼로그 관련 설정
        PlanDialog=new Dialog(context); //context로 하니까 잘 됩니다.
        PlanDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//제목 제거
        PlanDialog.setContentView(R.layout.group_dialog);

        dia_content = (TextView)PlanDialog.findViewById(R.id.dia_content);// setContentView에 대한 고찰..

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("Together_group_list");

        holder.Gname.setText(arrayList.get(position).getGname());
        holder.GAP.setText(String.valueOf(arrayList.get(position).getGAP()));
        holder.GCP.setText(String.valueOf(arrayList.get(position).getGCP()));

        //클릭 이벤트
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String GName = holder.Gname.getText().toString(); //그룹 이름을 저 변수에 담는다!
                showPlanDialog();
                dia_content.setText(GName+"에 가입하시겠습니까?");
            }
        });






    }

    @Override
    public int getItemCount() {
        //삼항 연산자, 배열이 비어있지 않으면 왼쪽이 실행!
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHoler extends RecyclerView.ViewHolder {
        //ImageView iv_people;
        TextView Gname;
        TextView GCP;
        TextView GAP;

        public CustomViewHoler(@NonNull View itemView) {
            super(itemView);
            //this.iv_people = itemView.findViewById(R.id.iv_people);
            this.Gname = itemView.findViewById(R.id.Gname);
            this.GAP = itemView.findViewById(R.id.GAP);
            this.GCP = itemView.findViewById(R.id.GCP);
        }
    }

    //그룹 가입 다이얼로그 호출(다이얼로그 관련 코드)
    public void showPlanDialog(){
        PlanDialog.show(); //다이얼로그 출력
        PlanDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//끝부분을 둥굴게 하기 위해 투명색 지정
        Button noBtn= PlanDialog.findViewById(R.id.noBtn);//취소 버튼
        Button yesBtn=PlanDialog.findViewById(R.id.yesBtn);//저장 버튼

        //취소 버튼
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlanDialog.dismiss();//다이얼로그 닫기
            }
        });

        //저장 버튼
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    //databaseReference=database.getReference().child("calendar").child(checkYear + "-" + checkMonth + "-" + checkDay);
                    //databaseReference.setValue(content);//선택한 날짜에 일정 저장
                    //Toast.makeText(view.getContext(), GName,Toast.LENGTH_SHORT).show(); //토스트로 실험

                }catch(Exception e){//예외
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext().getContext(),"오류발생",Toast.LENGTH_SHORT).show();//토스메세지 출력
                }
                PlanDialog.dismiss();//다이얼로그 닫기
            }
        });

    }

}
