package com.agenda.agendadecontatos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.agenda.agendadecontatos.dao.UsuarioDao
import com.agenda.agendadecontatos.databinding.ActivityCadastrarUsuarioBinding
import com.agenda.agendadecontatos.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CadastrarUsuario : AppCompatActivity() {

    private lateinit var binding: ActivityCadastrarUsuarioBinding
    private lateinit var usuarioDao: UsuarioDao
    private val listaUsuarios: MutableList<Usuario> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastrarUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btCadastrar.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {

                val nome      = binding.editNome.text.toString().trim()
                val sobrenome = binding.editSobrenome.text.toString().trim()
                val idade     = binding.editIdade.text.toString().trim()
                val celular   = binding.editCelular.text.toString().trim()
                var mensagem=0


                if (nome.isEmpty() || sobrenome.isEmpty() || idade.isEmpty() || celular.isEmpty()) {
                    mensagem = 1
                }
                else if (validar_nome_sobrenome(nome, sobrenome)!=0){
                    mensagem=validar_nome_sobrenome(nome,sobrenome)
                }
                else if(validar_idade(idade)!=0){
                    mensagem=validar_idade(idade)
                }
                else if(validar_celular(celular)!=0){
                    mensagem=validar_celular(celular)
                }

                else {
                    cadastrar(nome, sobrenome, idade, celular)
                }

                withContext(Dispatchers.Main) {
                    if (mensagem==0){
                        Toast.makeText(applicationContext, "Sucesso ao cadastrar usuário!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else if (mensagem==1){
                        Toast.makeText(applicationContext, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                    }
                    else if (mensagem==2){
                        Toast.makeText(applicationContext, "O campo Nome ou Sobrenome esta inválido!", Toast.LENGTH_SHORT).show()

                    }
                    else if (mensagem==3){
                        Toast.makeText(applicationContext, "O campo Idade esta inválido!", Toast.LENGTH_SHORT).show()

                    }
                    else if (mensagem==4){
                        Toast.makeText(applicationContext, "O campo Celular esta inválido!", Toast.LENGTH_SHORT).show()

                    }
                }
            }

        }
    }

    private fun cadastrar(nome: String, sobrenome: String, idade: String, celular: String){
      val usuario = Usuario(nome,sobrenome,idade,celular)
      listaUsuarios.add(usuario)
      usuarioDao = AppDatabase.getInstance(this).usuarioDao()
      usuarioDao.inserir(listaUsuarios)



    }
    private fun validar_nome_sobrenome(nome: String,sobrenome: String):Int {
        var a = 0
        val nome_e_sobrenome = nome + sobrenome
        if (nome_e_sobrenome.length > 35) {
            a = 2
        }
        else {
            for (x in nome_e_sobrenome) {
                if (!x.isLetter()) {
                    a = 2
                }

            }
        }
        return a

    }
    private fun validar_idade(idade: String):Int{
        var b=0
        val a = idade.toInt()
        if (a > 120) {
            b = 3
        }
        return b
    }


    private fun validar_celular(celular: String):Int{
        var b=0
        if (celular.length>15 || celular.length<8){
            b=4
        }
        return b
    }
}