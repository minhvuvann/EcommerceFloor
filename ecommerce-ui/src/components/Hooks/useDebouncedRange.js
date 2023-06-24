import React, { useState,useEffect } from 'react';


export function useDebouncedRange(delay) {
    const [value, setValue] = useState([0, 100]);
    const [debouncedValue, setDebouncedValue] = useState([0, 100]);
    let debounceTimer;
  
    useEffect(() => {
      debounceTimer = setTimeout(() => {
        setDebouncedValue(value);
      }, delay);
  
      return () => {
        clearTimeout(debounceTimer);
      };
    }, [value, delay]);
  
    function handleChange(event, newValue) {
      setValue(newValue);
    }
  
    return [debouncedValue, handleChange];
  }