import { attemptCode } from "../Services/services";
import { useMutation } from "@tanstack/react-query";
import { IAttempt } from "../Services/services";
import { useSolutionStore } from "../store";

export const useAttempt = () => {
  const { setExecuteTime, setResult, setVisible } = useSolutionStore();
  return useMutation({
    mutationFn: (data: IAttempt) => attemptCode(data),
    onSuccess: (data) => {
      setVisible();
      setResult(data.data.status);
      setExecuteTime(data.data.time);
    },
  });
};
