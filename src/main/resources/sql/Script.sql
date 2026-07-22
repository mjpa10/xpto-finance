CREATE OR REPLACE FUNCTION calcular_tarifa_movimentacao (
    p_cliente_id         IN NUMBER,
    p_data_movimentacao  IN DATE
) RETURN NUMBER
IS
    v_data_cadastro       DATE;
    v_dias_desde_cadastro NUMBER;
    v_inicio_periodo      DATE;
    v_fim_periodo         DATE;
    v_qtd_movimentacoes   NUMBER;
    v_tarifa              NUMBER(15,2);
BEGIN
    -- 1. busca a data de cadastro do cliente, que é o marco zero dos períodos de 30 dias
    SELECT DATA_CADASTRO INTO v_data_cadastro
    FROM CLIENTE
    WHERE ID = p_cliente_id;

    -- 2. descobre em qual "janela" de 30 dias a movimentação atual cai
    v_dias_desde_cadastro := TRUNC(p_data_movimentacao) - TRUNC(v_data_cadastro);
    v_inicio_periodo := TRUNC(v_data_cadastro) + FLOOR(v_dias_desde_cadastro / 30) * 30;
    v_fim_periodo    := v_inicio_periodo + 30;

    -- 3. conta quantas movimentações esse cliente já fez dentro dessa janela,
    --    somando todas as contas dele (join Conta -> Movimentacao)
    SELECT COUNT(*)
    INTO v_qtd_movimentacoes
    FROM MOVIMENTACAO M
    INNER JOIN CONTA C ON C.ID = M.CONTA_ID
    WHERE C.CLIENTE_ID = p_cliente_id
      AND M.DATA_MOVIMENTACAO >= v_inicio_periodo
      AND M.DATA_MOVIMENTACAO <  v_fim_periodo;

    -- +1 porque a movimentação atual ainda não foi salva no banco quando essa function é chamada
    v_qtd_movimentacoes := v_qtd_movimentacoes + 1;

    -- 4. aplica a faixa de tarifa
    IF v_qtd_movimentacoes <= 10 THEN
        v_tarifa := 1.00;
    ELSIF v_qtd_movimentacoes <= 20 THEN
        v_tarifa := 0.75;
    ELSE
        v_tarifa := 0.50;
    END IF;

    RETURN v_tarifa;
END;
/