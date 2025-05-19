export interface ErroAPI {
  timestamp: string;
  status: number;
  erro: string;
  mensagem: string;
  detalhes?: Record<string, string>;
}
