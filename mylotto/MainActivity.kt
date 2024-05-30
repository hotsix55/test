package com.example.mylotto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    private val clearButton by lazy { findViewById<Button>(R.id.btn_clear) }
    private val addButton by lazy { findViewById<Button>(R.id.btn_add) }
    private val runButton by lazy { findViewById<Button>(R.id.btn_run) }
    private val numPick by lazy { findViewById<NumberPicker>(R.id.np_num) }
    //사용자에게 이벤트받을 4가지를 먼저 선언함
    //공이 6개를 모아둘 리스트를 정의
    private val numTextViewList : List<TextView> by lazy {
        listOf<TextView>(
            findViewById(R.id.tv_num1)
            ,findViewById(R.id.tv_num2)
            ,findViewById(R.id.tv_num3)
            ,findViewById(R.id.tv_num4)
            ,findViewById(R.id.tv_num5)
            ,findViewById(R.id.tv_num6))
    }
    //run상태인제 체크를 하는 변수가 필요. 처음엔 실행이 아니니까 false
    private var didRun = false
    //사용자가 지정한 숫자를 잠깐 담궈둘 공간이 필요 hashset이라는 컬렉션 함수이용
    private val pickNumSet = hashSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //피커의 최대 최소값지정
        numPick.minValue = 1
        numPick.maxValue = 45
        //oncreate에서 다안하고 함수 3개만들거
        initRunButton()
        initAddButton()
        initClearButton()//여기까지가 메인함수
    }

    private fun initAddButton() {
        addButton.setOnClickListener {//addButton을 눌렀을때 {}가 실행이 되는거 {}에서할거는 번호 꽉차면 실행안하고 초기화해주세요 나오게끔하는거와 이미 존재하는숫자가 중복되서 추가되지않게끔할거. 상황에끔 하기위해 when이라는 코드사용
            when {
                didRun -> showToast("초기화 후에 시도해주세요.")
                pickNumSet.size >= 5 -> showToast("숫자는 최대 5개까지 선택할 수 있습니다.")
                pickNumSet.contains(numPick.value) -> showToast("이미 선택한 숫자입니다.")//예외는 끝
                else -> {
                    val textView = numTextViewList[pickNumSet.size]//textview는 방금뽑은숫자인데 이코드를 사용해 번호를 뽑는거같은데 이해는 잘안감..
                    textView.isVisible = true// 공이 보이게끔되고
                    textView.text = numPick.value.toString()// 공에 숫자를 대입

                    setNumBack(numPick.value, textView)//공을 만들었고
                    pickNumSet.add(numPick.value)//잠시보관하는 picknumberset에다 add하는것
                }
            }
        }
    }

    private fun setNumBack(number: Int, textView: TextView) {
        val background = when (number) {
            in 1..10 -> R.drawable.circle_yellow
            in 11..20 -> R.drawable.circle_blue
            in 21..30 -> R.drawable.circle_red
            in 31..40 -> R.drawable.circle_gray
            else -> R.drawable.circle_green
        }
        textView.background = ContextCompat.getDrawable(this, background)
    }
    //전부다 클리어 하는거
    private fun initClearButton() {
        clearButton.setOnClickListener {
            pickNumSet.clear()
            numTextViewList.forEach { it.isVisible = false }//visible끄는데 반복문 foreach사용해서 끄는거
            didRun = false
            numPick.value = 1//원래초기화값이 1이니까
        }
    }

    //run 자동생성을 시작하는거 기존에 아무거도없으면 6개생성, 만약에 2개잇으면 그것을 포함한 6개생성
    private fun initRunButton() {
        runButton.setOnClickListener {
            val list = getRandom()//랜덤값을 가지고옴
            didRun = true//6개를 다뿌렸으니까 트루

            list.forEachIndexed { index, number ->
                val textView = numTextViewList[index]
                textView.text = number.toString()
                textView.isVisible = true
                setNumBack(number, textView)//마무리코드
            }
        }
    }

    private fun getRandom(): List<Int> {
        val numbers = (1..45).filter { it !in pickNumSet }//1부터 45까지고를건데 내가 이미지정한 숫자를 빼고
        return (pickNumSet + numbers.shuffled().take(6 - pickNumSet.size)).sorted()//사용자가 먼저 설정한거를 뺀 1부터 45중에서 사용자가 지정한수만큼을 6에서 뺀값의 갯수만큼을 차례대로
    }//

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}