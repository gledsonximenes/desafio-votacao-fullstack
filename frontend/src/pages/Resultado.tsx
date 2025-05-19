import React, { useState, useEffect } from "react";
import Container from "@mui/material/Container";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import Alert from "@mui/material/Alert";
import Box from "@mui/material/Box";
import Paper from "@mui/material/Paper";
import CircularProgress from "@mui/material/CircularProgress";
import Grid from "@mui/material/Grid"; // já é o Grid V2 no MUI 7.x
import api from "../services/api";
import { ErroAPI } from "../types/ErroAPI";
import { useSearchParams } from 'react-router-dom';

interface ResultadoDTO {
  pautaId: number;
  assunto: string;
  votosSim: number;
  votosNao: number;
  resultado: string;
}

const Resultado: React.FC = () => {
  const [searchParams] = useSearchParams();
  const [pautaId, setPautaId] = useState<number | "">("");
  const [data, setData] = useState<ResultadoDTO | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const fetchResultado = async (id: number) => {
    setLoading(true);
    setError(null);
    try {
      const res = await api.get<ResultadoDTO>(`/sessoes/${id}/resultado`);
      setData(res.data);
    } catch (err: unknown) {
      const erro = err as ErroAPI;
      setError(erro.mensagem);
      setData(null);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const pautaIdParam = searchParams.get("pautaId");
    if (pautaIdParam) {
      const id = Number(pautaIdParam);
      setPautaId(id);
      fetchResultado(id);
    }
  }, [searchParams]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!pautaId) {
      setError("Informe o ID da pauta");
      setData(null);
      return;
    }
    await fetchResultado(pautaId);
  };

  return (
    <Container maxWidth="sm" sx={{ mt: 4 }}>
      <Typography variant="h5" gutterBottom>
        Resultado da Votação
      </Typography>

      <Box
        component="form"
        onSubmit={handleSubmit}
        sx={{ display: "flex", gap: 2, mb: 2 }}
      >
        <TextField
          type="number"
          label="ID da Pauta"
          value={pautaId}
          onChange={(e) => setPautaId(Number(e.target.value))}
          fullWidth
          required
        />
        <Button
          variant="contained"
          type="submit"
          disabled={loading}
          sx={{ minWidth: 120 }}
        >
          {loading ? <CircularProgress size={24} /> : "Buscar"}
        </Button>
      </Box>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      {data && (
        <Paper elevation={3} sx={{ p: 3 }}>
          <Grid container spacing={2}>
            <Grid>
              <Typography>
                <strong>Assunto:</strong> {data.assunto}
              </Typography>
            </Grid>
            <Grid>
              <Typography color="success.main">
                👍 Sim: {data.votosSim}
              </Typography>
            </Grid>
            <Grid>
              <Typography color="error.main">
                👎 Não: {data.votosNao}
              </Typography>
            </Grid>
            <Grid>
              <Typography>
                <strong>Resultado:</strong>{" "}
                <Box
                  component="span"
                  sx={{ fontWeight: "bold", color: "primary.main" }}
                >
                  {data.resultado}
                </Box>
              </Typography>
            </Grid>
          </Grid>
        </Paper>
      )}
    </Container>
  );
};

export default Resultado;