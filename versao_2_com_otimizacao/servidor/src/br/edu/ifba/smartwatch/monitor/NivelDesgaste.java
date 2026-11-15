package br.edu.ifba.smartwatch.monitor;

/**
 * Enumeração abstrata para classificar o diagnóstico biomecânico resultante
 * da análise de tríades de degradação de cadência (Funcionalidade d.4).
 */

public enum NivelDesgaste {
    ESTAVEL,
    ALERTA_MODERADO,
    RISCO_CRITICO_LESAO
}
