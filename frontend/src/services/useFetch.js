/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from "react";
import axios from "axios";

/**
 * Custom hook for fetching data using axios.
 *
 * @param {string} url - The endpoint URL to fetch data from.
 * @param {boolean} [lazy=false] - Whether to fetch data on mount or only when calling refresh function.
 * @param header - The header to be used in the request. for example: { 'X-Auth-Token': `${API.API_KEY}` }
 * @returns {Object} An object containing the fetched data, loading state, error, and a refresh function.
 */
const useFetch = (url, lazy = false, header) => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);


  useEffect(() => {
    if (!lazy) {
      setLoading(true);
      axios
        .get(url, { headers: header })
        .then((res) => {
          if (res.data === null) {
            setError({ status: 404 });
          } else {
            setData(res.data);
          }
        })
        .catch((err) => {
          setError(err);
        })
        .finally(() => {
          setLoading(false);
        });
    }
  }, [url, lazy]);

  const refresh = () => {
    setLoading(true);
    axios
      .get(url, { headers: header })
      .then((res) => {
        setData(res.data);
      })
      .catch((err) => {
        setError(err);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  return { data, loading, error, refresh };
};

export default useFetch;
