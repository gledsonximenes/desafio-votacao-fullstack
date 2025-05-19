import React, { useState, useEffect } from "react";
import {
  Container,
  Typography,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Button,
  Alert,
  Box,
  Paper,
  CircularProgress,
  Grid,
} from "@mui/material";
import api from "../services/api";
import { ErroAPI } from "../types/ErroAPI";

interface ResultadoDTO {
  pautaId: number;
  assunto: string;
  votosSim: number;
  votosNao: number;
  resultado: string;
}

interface PautaDTO {
  id: number;
  assunto: string;
}

const Resultado: React.FC = () => {
  const [pautas, setPautas] = useState<PautaDTO[]>([]);
  const [pautaId, setPautaId] = useState<number | "">("");
  const [data, setData] = useState<ResultadoDTO | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    api.get<PautaDTO[]>("/pautas")
      .then(res => setPautas(res.data))
      .catch(() => {
      });
  }, []);

  const fetchResultado = async (id: number) => {
    setLoading(true);
    setError(null);
    try {
      const res = await api.get<ResultadoDTO>(`/sessoes/${id}/resultado`);
      setData(res.data);
    } catch (err: unknown) {
      const erro = err as ErroAPI;
      setError(erro.mensagem || "Erro ao buscar resultado");
      setData(null);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!pautaId) {
      setError("Selecione uma pauta");
      setData(null);
      return;
    }
    fetchResultado(pautaId as number);
  };

  return (
    <Container maxWidth="sm" sx={{ mt: 4 }}>
      <Typography variant="h5" gutterBottom>
        Resultado da Votação
      </Typography>

      <Box component="form" onSubmit={handleSubmit} sx={{ mb: 2 }}>
        <FormControl fullWidth required sx={{ mb: 2 }}>
          <InputLabel id="select-pauta-label">Pauta</InputLabel>
          <Select
            labelId="select-pauta-label"
            id="select-pauta"
            value={pautaId}
            label="Pauta"
            onChange={(e) => setPautaId(e.target.value as number)}
          >
            <MenuItem value="">
              <em>Selecione...</em>
            </MenuItem>
            {pautas.map((p) => (
              <MenuItem key={p.id} value={p.id}>
                {p.id} – {p.assunto}
              </MenuItem>
            ))}
          </Select>
        </FormControl>

        <Button
          variant="contained"
          type="submit"
          disabled={loading}
          fullWidth
          sx={{ minHeight: 48 }}
        >
          {loading ? <CircularProgress size={24} /> : "Buscar Resultado"}
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