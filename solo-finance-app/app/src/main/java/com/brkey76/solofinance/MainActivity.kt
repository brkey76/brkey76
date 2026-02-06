package com.brkey76.solofinance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.brkey76.solofinance.ui.theme.SoloFinanceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoloFinanceTheme {
                SoloFinanceApp()
            }
        }
    }
}

enum class CategoriaLancamento(val label: String, val pontos: Int) {
    SALARIO("Salário", 10),
    RENDA_EXTRA("Renda Extra", 15),
    GASTO_PLANEJADO("Gasto Planejado", 5),
    GASTO_NAO_PLANEJADO("Gasto Não Planejado", -10),
    INVESTIMENTO("Investimento", 20)
}

data class Lancamento(
    val descricao: String,
    val valor: Double,
    val categoria: CategoriaLancamento
)

class FinanceViewModel : ViewModel() {
    private val _lancamentos = mutableStateOf(listOf<Lancamento>())
    val lancamentos = _lancamentos

    val saldoAtual: Double
        get() = _lancamentos.value.sumOf {
            when (it.categoria) {
                CategoriaLancamento.GASTO_PLANEJADO,
                CategoriaLancamento.GASTO_NAO_PLANEJADO -> -it.valor
                else -> it.valor
            }
        }

    val pontos: Int
        get() = _lancamentos.value.sumOf { it.categoria.pontos }.coerceAtLeast(0)

    fun addLancamento(item: Lancamento) {
        _lancamentos.value = _lancamentos.value + item
    }

    fun nivel(): String = when {
        pontos >= 150 -> "Sombra Monarca"
        pontos >= 100 -> "Caçador Rank S"
        pontos >= 60 -> "Caçador Rank A"
        pontos >= 30 -> "Caçador Rank B"
        else -> "Novato"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoloFinanceApp(vm: FinanceViewModel = viewModel()) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Solo Finance Leveling") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatusCard(
                saldo = vm.saldoAtual,
                pontos = vm.pontos,
                nivel = vm.nivel()
            )
            FormLancamento(onSubmit = vm::addLancamento)
            HistoricoLista(vm.lancamentos.value)
        }
    }
}

@Composable
fun StatusCard(saldo: Double, pontos: Int, nivel: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Saldo atual: R$ %.2f".format(saldo), style = MaterialTheme.typography.titleMedium)
            Text("Pontos: $pontos")
            Text("Nível: $nivel")
        }
    }
}

@Composable
fun FormLancamento(onSubmit: (Lancamento) -> Unit) {
    var descricao by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = descricao,
            onValueChange = { descricao = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Descrição") }
        )
        OutlinedTextField(
            value = valor,
            onValueChange = { valor = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Valor") }
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
            items(CategoriaLancamento.entries.toList()) { categoria ->
                Button(
                    onClick = {
                        val valorDouble = valor.toDoubleOrNull() ?: 0.0
                        if (descricao.isNotBlank() && valorDouble > 0.0) {
                            onSubmit(Lancamento(descricao, valorDouble, categoria))
                            descricao = ""
                            valor = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(categoria.label)
                        Text("${categoria.pontos} pts")
                    }
                }
            }
        }
    }
}

@Composable
fun HistoricoLista(lancamentos: List<Lancamento>) {
    Text("Histórico", style = MaterialTheme.typography.titleMedium)
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(lancamentos.reversed()) { item ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(item.descricao, style = MaterialTheme.typography.titleSmall)
                    Text("Categoria: ${item.categoria.label}")
                    Text("Valor: R$ %.2f".format(item.valor))
                }
            }
        }
    }
}
