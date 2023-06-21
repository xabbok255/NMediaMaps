package com.xabbok.nmediamaps.presentation.fragments

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.MultiAutoCompleteTextView
import android.widget.NumberPicker
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RatingBar
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import android.widget.ToggleButton
import androidx.appcompat.widget.SwitchCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.descendants
import androidx.core.view.updateLayoutParams
import androidx.core.widget.CompoundButtonCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.navigation.NavigationView
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.tabs.TabLayout
import com.xabbok.nmediamaps.R
import com.xabbok.nmediamaps.databinding.FragmentColorsBinding
import com.xabbok.nmediamaps.dto.ThemeColors
import com.xabbok.nmediamaps.dto.generateThemeColors

class ColorsFragment : Fragment(R.layout.fragment_colors) {
    private val binding: FragmentColorsBinding by viewBinding(FragmentColorsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.hueSeekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val color: Int = Color.HSVToColor(floatArrayOf(progress.toFloat(), 1f, 1f))

                val themeColor = generateThemeColors(color)

                /*binding.colorPrimaryView.setBackgroundColor(themeColor.colorPrimary)
                binding.colorPrimaryVariantView.setBackgroundColor(themeColor.colorPrimaryVariant)
                binding.themeColors.colorSecondaryView.setBackgroundColor(themeColor.themeColors.colorSecondary)
                binding.themeColors.colorSecondaryVariantView.setBackgroundColor(themeColor.themeColors.colorSecondaryVariant)
                binding.colorOnPrimaryView.setBackgroundColor(themeColor.colorOnPrimary)
                binding.colorOnSecondaryView.setBackgroundColor(themeColor.colorOnSecondary)*/

                binding.viewList1.removeAllViewsInLayout()
                binding.viewList2.removeAllViewsInLayout()

                addColorViews(linearLayout = binding.viewList1, themeColors = themeColor)
                applyThemeColors(layoutView = binding.viewList2, themeColors = themeColor)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })
    }

    fun addColorViews(linearLayout: LinearLayout, themeColors: ThemeColors) {
        val fields =
            themeColors.javaClass.declaredFields.filter { it.name.startsWith("color") && it.type == Int::class.java }

        for (field in fields) {
            field.isAccessible = true    // установка флага доступности поля
            val colorValue = field.getInt(themeColors)    // получение значения цвета из поля

            val fieldName = field.name    // получение названия поля
            val textView = TextView(linearLayout.context)
            textView.text = fieldName

            val colorView = View(linearLayout.context)
            colorView.setBackgroundColor(colorValue)

            linearLayout.addView(textView)
            linearLayout.addView(
                colorView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120)
            )   // размеры View можно менять по желанию
        }
    }

    @SuppressLint("SetTextI18n", "DiscouragedApi")
    fun applyThemeColors(themeColors: ThemeColors, layoutView: LinearLayout) {
        val context = requireContext()

        val textView = TextView(context).apply {
            text = "TextView"; setTextColor(themeColors.colorOnSurface); setBackgroundColor(
            themeColors.colorSurface
        )
        }
        val editText = EditText(context).apply {
            hint = "EditText"; setTextColor(themeColors.colorOnSurface); setBackgroundColor(
            themeColors.colorSurface
        ); setHintTextColor(themeColors.colorOnSurface)
        }
        val button = Button(context).apply {
            text = "Button"; setTextColor(themeColors.colorOnPrimary); setBackgroundColor(
            themeColors.colorPrimary
        )
        }
        val imageButton =
            ImageButton(context).apply { setBackgroundColor(themeColors.colorPrimary) }
        val imageView = ImageView(context).apply {
            setImageResource(R.drawable.ic_launcher_background)
            val colorFilter = PorterDuffColorFilter(
                themeColors.colorSurface,
                PorterDuff.Mode.SRC_IN
            ); setColorFilter(colorFilter)
        }
        val checkBox = CheckBox(context).apply {
            text = "CheckBox"
            val stateList = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_enabled),
                    intArrayOf(android.R.attr.state_checked),
                    intArrayOf(-android.R.attr.state_checked)
                ),
                intArrayOf(
                    themeColors.colorControlNormal,
                    themeColors.colorBrandAccent,
                    themeColors.colorControlNormal
                )
            ); CompoundButtonCompat.setButtonTintList(this, stateList)
        }
        val switchCompat = SwitchCompat(context).apply {
            text = "Switch"
            val thumbColor =
                if (isChecked) themeColors.colorBrandAccent else themeColors.colorControlNormal
            val trackColor =
                if (isChecked) themeColors.colorBrandAccent else themeColors.colorControlActivated; thumbTintList =
            ColorStateList.valueOf(thumbColor); trackTintList = ColorStateList.valueOf(trackColor)
        }
        val radioButton = RadioButton(context).apply {
            text = "RadioButton"
            val stateList = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_enabled),
                    intArrayOf(android.R.attr.state_checked),
                    intArrayOf(-android.R.attr.state_checked)
                ),
                intArrayOf(
                    themeColors.colorControlNormal,
                    themeColors.colorBrandAccent,
                    themeColors.colorControlNormal
                )
            ); CompoundButtonCompat.setButtonTintList(this, stateList)
        }
        val seekBar = SeekBar(context).apply {
            progressTintList = ColorStateList.valueOf(themeColors.colorPrimary); thumbTintList =
            ColorStateList.valueOf(themeColors.colorBrandAccent)
        }


        val ratingBar = RatingBar(context).apply {
            val progressDrawable = requireNotNull(progressDrawable) as LayerDrawable
            DrawableCompat.setTint(DrawableCompat.wrap(progressDrawable.getDrawable(0)), themeColors.colorPrimary)
            DrawableCompat.setTint(DrawableCompat.wrap(progressDrawable.getDrawable(1)), themeColors.colorSurface)
            DrawableCompat.setTint(DrawableCompat.wrap(progressDrawable.getDrawable(2)), themeColors.colorSecondary)
        }



        val progressBar = ProgressBar(context).apply {
            indeterminateTintList =
                ColorStateList.valueOf(themeColors.colorBrandAccent); progressTintList =
            ColorStateList.valueOf(themeColors.colorPrimary)
        }
        val toggleButton = ToggleButton(context)
        toggleButton.apply {
            textOn = "ON"
            textOff = "OFF"
            val textColorStateList = ColorStateList(
                arrayOf(
                    intArrayOf(android.R.attr.state_checked),
                    intArrayOf(-android.R.attr.state_checked)
                ),
                intArrayOf(
                    themeColors.colorOnBackground,
                    themeColors.colorOnBackground
                )
            )
            CompoundButtonCompat.setButtonTintList(this, textColorStateList)
            val backgroundDrawable = GradientDrawable()
            backgroundDrawable.setColor(themeColors.colorSurface)
            backgroundDrawable.setStroke(2, themeColors.colorControlNormal)
            background = backgroundDrawable
            val thumbDrawable = GradientDrawable()
            thumbDrawable.shape = GradientDrawable.OVAL
            thumbDrawable.setColor(themeColors.colorBrandAccent)
            thumbDrawable.setStroke(2, themeColors.colorControlNormal)
            thumbDrawable.setSize(60, 60)
            thumbDrawable.setBounds(0, 0, 60, 60)
            thumbDrawable.setColorFilter(
                PorterDuffColorFilter(
                    themeColors.colorBrandAccent,
                    PorterDuff.Mode.SRC_IN
                )
            )
            thumbDrawable.callback = this
            try {
                // Set thumb drawable using reflection on API levels below 16
                val thumbField = ToggleButton::class.java.getDeclaredField("mThumbDrawable")
                thumbField.isAccessible = true
                thumbField.set(this, thumbDrawable)
            } catch (e: Exception) {
                // Ignore exception if reflection fails
            }
            val stateListDrawable = StateListDrawable()
            stateListDrawable.addState(
                intArrayOf(-android.R.attr.state_checked),
                backgroundDrawable
            )
            stateListDrawable.addState(
                intArrayOf(android.R.attr.state_checked),
                backgroundDrawable.apply {
                    setColor(themeColors.colorBrandAccent)
                }
            )
            setButtonDrawable(stateListDrawable)
        }


        val switchMaterial = SwitchMaterial(context).apply {
            text = "Switch Material"; thumbTintList =
            ColorStateList.valueOf(themeColors.colorBrandAccent); trackTintList =
            ColorStateList.valueOf(themeColors.colorControlActivated)
        }
        val radioButtonMaterial = MaterialRadioButton(context).apply {
            text = "Radio Button Material"
            val stateList = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_enabled),
                    intArrayOf(android.R.attr.state_checked),
                    intArrayOf(-android.R.attr.state_checked)
                ),
                intArrayOf(
                    themeColors.colorControlNormal,
                    themeColors.colorBrandAccent,
                    themeColors.colorControlNormal
                )
            ); buttonTintList = stateList
        }
        val checkBoxMaterial = MaterialCheckBox(context).apply {
            text = "Check Box Material"
            val stateList = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_enabled),
                    intArrayOf(android.R.attr.state_checked),
                    intArrayOf(-android.R.attr.state_checked)
                ),
                intArrayOf(
                    themeColors.colorControlNormal,
                    themeColors.colorBrandAccent,
                    themeColors.colorControlNormal
                )
            ); buttonTintList = stateList
        }

        val datePicker = DatePicker(context).apply { setBackgroundColor(themeColors.colorSurface) }
        val timePicker = TimePicker(context).apply {
            setBackgroundColor(themeColors.colorSurface)

            // Set the text color and divider color of NumberPicker using resources and reflection
            val textColor = themeColors.colorPrimary
            try {
                val fieldIds = arrayOf("hour", "minute")
                for (fieldId in fieldIds) {
                    val field = TimePicker::class.java.getDeclaredField("m${fieldId}Spinner")
                    field.isAccessible = true
                    val numberPicker = field.get(this) as? NumberPicker ?: continue

                    // Set the background and divider drawables for the NumberPicker
                    val attrs = intArrayOf(android.R.attr.listDivider)
                    val ta = context.obtainStyledAttributes(attrs)
                    val divider = ta.getDrawable(0)?.mutate()
                    ta.recycle()
                    if (divider != null) {
                        @Suppress("DEPRECATION")
                        divider.setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP)
                        numberPicker.dividerDrawable = divider
                    }

                    // Set the text color of the EditText view inside the NumberPicker
                    val editTextId =
                        Resources.getSystem().getIdentifier("numberpicker_input", "id", "android")
                    findViewById<EditText>(editTextId)?.setTextColor(textColor)
                    findViewById<TextView>(editTextId)?.setTextColor(textColor)
                }
            } catch (e: Exception) {
                // Handle exception
            }
        }


        val bottomNavigationView = BottomNavigationView(context).apply {
            setBackgroundColor(themeColors.colorPrimary); itemIconTintList =
            ColorStateList.valueOf(themeColors.colorOnPrimary); itemTextColor =
            ColorStateList.valueOf(themeColors.colorOnPrimary)
        }
        val navigationView =
            NavigationView(context).apply { setBackgroundColor(themeColors.colorSurface) }
        val tabLayout = TabLayout(context).apply {
            setSelectedTabIndicatorColor(themeColors.colorBrandAccent); setTabTextColors(
            themeColors.colorOnBackground,
            themeColors.colorBrandAccent
        ); setBackgroundColor(themeColors.colorSurface)
        }
        val viewPager = ViewPager(context).apply { setBackgroundColor(themeColors.colorSurface) }
        val recyclerView =
            RecyclerView(context).apply { setBackgroundColor(themeColors.colorSurface) }
        val listView = ListView(context).apply { setBackgroundColor(themeColors.colorSurface) }
        val gridView = GridView(context).apply { setBackgroundColor(themeColors.colorSurface) }
        val spinner = Spinner(context).apply { setBackgroundColor(themeColors.colorSurface) }
        val autoCompleteTextView =
            AutoCompleteTextView(context).apply { setBackgroundColor(themeColors.colorSurface) }
        val multiAutoCompleteTextView =
            MultiAutoCompleteTextView(context).apply { setBackgroundColor(themeColors.colorSurface) }

        // Добавление всех View на LinearLayout
        // Add all created views to the layout
        layoutView.addView(textView)
        layoutView.addView(editText)
        layoutView.addView(button)
        layoutView.addView(imageButton)
        layoutView.addView(imageView)
        layoutView.addView(checkBox)
        layoutView.addView(switchCompat)
        layoutView.addView(radioButton)
        layoutView.addView(seekBar)
        layoutView.addView(ratingBar)
        layoutView.addView(progressBar)
        layoutView.addView(toggleButton)
        layoutView.addView(switchMaterial)
        layoutView.addView(radioButtonMaterial)
        layoutView.addView(checkBoxMaterial)
        layoutView.addView(datePicker)
        layoutView.addView(timePicker)
        layoutView.addView(bottomNavigationView)
        layoutView.addView(navigationView)
        layoutView.addView(tabLayout)
        layoutView.addView(viewPager)
        layoutView.addView(recyclerView)
        layoutView.addView(listView)
        layoutView.addView(gridView)
        layoutView.addView(spinner)
        layoutView.addView(autoCompleteTextView)
        layoutView.addView(multiAutoCompleteTextView)


        // Установка цвета фона для всех View внутри LinearLayout
        layoutView.descendants.forEach { view ->
            view.setBackgroundColor(themeColors.colorBackground)

            if (view.layoutParams is LinearLayout.LayoutParams) {
                view.updateLayoutParams<LinearLayout.LayoutParams> {
                    topMargin = 30
                }
            }
        }

        // Установка цвета фона для самого LinearLayout
        //layoutView.setBackgroundColor(themeColors.colorBackground)
    }
}
