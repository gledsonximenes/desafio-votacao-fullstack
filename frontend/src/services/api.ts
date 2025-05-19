import axios from 'axios';
import { ErroAPI } from '../types/ErroAPI';

const api = axios.create({
  baseURL: 'http://localhost:8080/api/v1',
});

api.interceptors.response.use(
  response => response,
  error => {
    const data = error.response?.data;
    if (data?.mensagem) {
      const erro: ErroAPI = {
        timestamp: data.timestamp || new Date().toISOString(),
        status: data.status || 500,
        erro: data.erro || 'Erro',
        mensagem: data.mensagem || 'Erro desconhecido',
        detalhes: data.detalhes || undefined,
      };
      return Promise.reject(erro);
    }

    const erro: ErroAPI = {
      timestamp: new Date().toISOString(),
      status: 500,
      erro: 'Erro Desconhecido',
      mensagem: 'Erro inesperado. Tente novamente mais tarde.',
    };

    return Promise.reject(erro);
  }
);

export default api;
