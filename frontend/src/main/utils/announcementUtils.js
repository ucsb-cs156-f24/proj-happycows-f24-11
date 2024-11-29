import { toast } from 'react-toastify';

export function onDeleteSuccess(message) {
  console.log(message);
  toast(message);
}

export function cellToAxiosParamsDelete(cell) {
  return {
    url: `/api/announcements/delete/${cell.row.values.id}`,
    method: 'DELETE',
  };
}
